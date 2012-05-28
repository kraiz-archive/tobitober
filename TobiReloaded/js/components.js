/**
 * Our Hero
 */
Crafty.c('Tobi', {

    init: function() {
        this.requires('spriteTobi, SpriteAnimation, Gravity, Twoway, Collision, Keyboard');
        this.twoway(5, 12);
        this.gravity('Solid');
        this.gravityConst(0.2);
        this.collision();
        this.bind('Moved', function(from) {
            if(this.hit('Solid')) {
                this.attr({x: from.x, y: from.y});
            }
        });
        this.onHit('Solid', function(hit) {
            for (var i = 0; i < hit.length; i++) {
                log(hit[i].normal);
            }
        });
        this.bind('KeyDown', function(e) {
            if (e.key == Crafty.keys['W'] || e.key == Crafty.keys['UP_ARROW']) {
                if (!this._falling) {
                    Crafty.audio.play('jump');
                }
            }
        });
        this.bind('TobiDead', function() {
            if (e.key == Crafty.keys['W'] || e.key == Crafty.keys['UP_ARROW']) {
                if (!this._falling) {
                    Crafty.audio.play('jump');
                }
            }
        });
        this.bind('EnterFrame', function() {
            // Camera Stuff
            var vp = {
                x: Crafty.viewport.width/2 - this.x - this.w/2,
                y: Crafty.viewport.height/2 - this.y - this.h/2
            };
            if (vp.x != Crafty.viewport.x) { Crafty.viewport.scroll('_x', vp.x); }
            if (vp.y != Crafty.viewport.y) { Crafty.viewport.scroll('_y', vp.y); }

            // Sprite Animation
            var left = this.isDown('LEFT_ARROW') || this.isDown('A'),
                right = this.isDown('RIGHT_ARROW') || this.isDown('S'),
                up = this._up,
                stand = !left && !right && !up;
            if (!left && !right) {
                left = this._currentReelId.split('_')[2] === 'left';
            }
            var anim = 'tobi_' + (stand ? 'stand' : up ? 'jump' : 'walk') + '_' + (left ? 'left' : 'right');
            if (!this.isPlaying(anim)) {
                this.stop().animate(anim, 10, -1);
            }
        });
        this.animate('tobi_stand_right', 0, 0, 0);
        this.animate('tobi_stand_left', 4, 0, 4);
        this.animate('tobi_walk_right', 0, 0, 3);
        this.animate('tobi_walk_left', 4, 0, 7);
        this.animate('tobi_jump_left', 8, 0, 8);
        this.animate('tobi_jump_right', 9, 0, 9);
        this.animate('tobi_jump_right', 10, -1);

    }
});

/**
 * The famous "Maerzdorfer High Performance Onion"
 * BEWARE: Not for childrn, pretty strong stuff!
 */
Crafty.c('Onion', {

    init: function() {
        this.requires('spriteOnion, Collision, SpriteAnimation');
        this.collision();
        this.animate('onion_waggling', 0, 0, 3);
        this.animate('onion_waggling', 25, -1);
        this.onHit('Tobi', function(hits) {
            for (var i = 0; i < hits.length; i++) {
                this.destroy();
                addPoints(10);
                Crafty.audio.play('onion');
            }
        });
    }
});

//------------------------------------------------------------------------------
//  Kind of abstract Components that'll handle common behavior
//------------------------------------------------------------------------------
/**
 * The common evil and unpredictable movement of most of the alien invaders
 */
Crafty.c('Runner', {

    _left: false,
    _canMove: true,
    _platformEdgeDetector: null,

    init: function() {
        this.requires('Collision');
        this.bind('EnterFrame', function() {
            if (this._canMove) {
                // Movement
                var step = config.monsters.runner.speed;
                if (this._left) {
                    this.x -= step;
                    this._platformEdgeDetector.shift(-step, 0);
                } else {
                    this.x += step;
                    this._platformEdgeDetector.shift(step, 0);
                }
            }
        });
        this.bind('ToggleDirection', function() {
            this._left = !this._left;
            this._platformEdgeDetector.shift(this._left ? -34 : +34, 0);
        });
        this.onHit('Solid', function() {
            this.trigger('ToggleDirection');
        });
    },

    runner: function(left) {
        this._left = left;
        this._platformEdgeDetector = Crafty.e(E + 'Collision').attr({
            x: this.x + (this._left ? -1 : +33),
            y: this.y + 32,
            w: 4,
            h: 4,
            z: 5,
            runner: this
        });
        this._platformEdgeDetector.onHit('Solid', function() {}, function() {
            this.runner.trigger('ToggleDirection');
        });
        this.trigger('ToggleDirection');
    }
});

/**
 * The common evil and unpredictable movement of most of the alien invaders
 */
Crafty.c('Clompable', {

    _dead: false,

    init: function() {
        this.requires('Runner, Collision');
        this.onHit('Tobi', function(hits) {
            if (!this._dead) {
                for ( var i = 0; i < hits.length; i++) {
                    var tobi = hits[i].obj;
                    if (tobi._falling) {
                        this._dead = true;
                        this._canMove = false;
                        this.stop().animate('dead', 1, -1);
                        addPoints(25);
                        Crafty.audio.play(
                            this.has('Schnappi') ? 'schnappi' :
                            this.has('Kroko') ? 'kroko' : 'nsa',
                        1, 0.5);
                    } else {
                        tobi.trigger('TobiDead');
                    }
                }
            }
        });
    }

});

//------------------------------------------------------------------------------
//  Everything above are evil monsters - yes, even the water!
//------------------------------------------------------------------------------

/**
 * The most cruel liquid in the game!
 */
Crafty.c('Water', {

    init: function() {
        this.requires('spriteWater, SpriteAnimation')
            .animate('water_wobbling', 0, 0, 2)
            .animate('water_wobbling', Crafty.math.randomInt(config.monsters.water.min, config.monsters.water.max), -1);
    }
});



/**
 * Who named this yukky thing "schnappi"?!
 */
Crafty.c('Schnappi', {

    init: function() {
        this.requires('spriteSchnappi, Runner, SpriteAnimation')
            .animate('schnappi_right', 0, 0, 2)
            .animate('schnappi_left', 3, 0, 5)
            .animate('dead', 6, 0, 6);
        this.bind('ToggleDirection', function() {
            this.stop().animate('schnappi_' + (this._left ? 'left' : 'right'), 15, -1);}
        );
    }
});

/**
 * The wicked crocodile of death on roller skates!
 */
Crafty.c('Kroko', {

    init: function() {
        this.requires('spriteKroko, Runner, SpriteAnimation')
            .animate('kroko_right', 0, 0, 2)
            .animate('kroko_left', 3, 0, 5)
            .animate('dead', 6, 0, 6);
        this.bind('ToggleDirection', function() {
            this.stop().animate('kroko_' + (this._left ? 'left' : 'right'), 15, -1);
        });
    }
});

/**
 * The wicked crocodile of death on roller skates!
 */
Crafty.c('Ghost', {

    _follow: null,

    init: function() {
        this.requires('spriteGhost, SpriteAnimation')
            .animate('ghost_floating', 0, 0, 1)
            .animate('ghost_floating', 10, -1);
        this.bind('EnterFrame', function() {
            var step = config.monsters.follower.speed;
            if (this._follow.x !== this.x) {
                this.x += this._follow.x > this.x ? +step : -step;
            }
            if (this._follow.y !== this.y) {
                this.y += this._follow.y > this.y ? +step : -step;
            }
        });
    },

    follow: function(follow) {
        this._follow = follow;
    }
});

/**
 * Is this guy gay?
 */
Crafty.c('Nsa', {

    init: function() {
        this.requires('spriteNsa, Runner, SpriteAnimation')
            .animate('nsa_right', 0, 0, 1)
            .animate('nsa_left', 2, 0, 3)
            .animate('dead', 4, 0, 4);
        this.bind('ToggleDirection', function() {
            this.stop().animate('nsa_' + (this._left ? 'left' : 'right'), 15, -1);
        });
    }
});

/**
 * Steely tank with nasty spikes on its head - a crazy alien robot
 */
Crafty.c('Robot', {

    init: function() {
        this.requires('spriteRobot, Runner, SpriteAnimation')
            .animate('robot_right', 0, 0, 1)
            .animate('robot_left', 2, 0, 3);
        this.bind('ToggleDirection', function() {
            this.stop().animate('robot_' + (this._left ? 'left' : 'right'), 15, -1);
        });
    }
});

/**
 * The tiny squirrel, our friend and helper
 */
Crafty.c('Zwiebli', {

    init: function() {
        this.requires('spriteZwiebli, SpriteAnimation')
            .animate('zwiebli_right', 0, 0, 1)
            .animate('zwiebli_left', 2, 0, 3);
        this.bind('ToggleDirection', function() {
            this.stop().animate('zwiebli_' + (this._left ? 'left' : 'right'), 15, -1);
        });
    }
});

/**
 * The huge and ugly squirrel cousin
 */
Crafty.c('Zwiablo', {

    init: function() {
        this.requires('spriteZwiablo, Runner, SpriteAnimation')
            .animate('zwiablo_right', 0, 0, 1)
            .animate('zwiablo_left', 2, 0, 3);
        this.bind('ToggleDirection', function() {
            this.stop().animate('zwiablo_' + (this._left ? 'left' : 'right'), 15, -1);
        });
    }
});