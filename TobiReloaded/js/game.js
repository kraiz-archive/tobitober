var CURRENT_MAP = [],
    TOBI = null;

/*
 * Main menu on load
 */
window.onload = function() {
    Crafty.init(config.stageWidth, config.stageHeight);
    if (config.config === 'Canvas') {
        Crafty.canvas.init();
    }
    Crafty.load(['gfx/play.png', 'gfx/presents.png', 'gfx/exit.png', 'gfx/z-crew.png', 'gfx/header.png'], function() {
        Crafty.scene('menu', function() {
            Crafty.background('#000');
            setTimeout(function() {
                Crafty.e('2D, ' + config.renderType + ', Image').attr({x: 240, y: 10}).image('gfx/z-crew.png');
            }, 500);
            setTimeout(function() {
                Crafty.e('2D, ' + config.renderType + ', Image').attr({x: 190, y: 50}).image('gfx/presents.png');
            }, 1000);
            setTimeout(function() {
                Crafty.e('2D, ' + config.renderType + ', Image').attr({x: 170, y: 150}).image('gfx/header.png');
            }, 1500);
            setTimeout(function() {
                Crafty.e('2D, ' + config.renderType + ', Image, KeyBoard, Mouse').attr({x: 242, y: 300}).image('gfx/play.png').bind('KeyDown', function(e) {
                    if (e.keyCode === Crafty.keys['P']) {
                        loadLevel(1);
                    }
                });
                Crafty.e('2D, ' + config.renderType + ', Image').attr({x: 242, y: 350}).image('gfx/exit.png').requires('KeyBoard').bind('KeyDown', function(e) {
                    if (e.keyCode === Crafty.keys['E']) {
                        Crafty.scene('menu');
                    }
                });
            }, 2000);
        });
        //Crafty.scene('menu');
        loadLevel(1);
    });

};

/**
 * Loads the common sprites
 */
window.loadSprites = function() {
    for ( var sprite in mapConfig['common'].sprites) {
        Crafty.sprite(32, sprite, mapConfig['common'].sprites[sprite]);
    }
};

/**
 * Level loader
 */
window.loadLevel = function(level) {
    log('Loading level ' + level);
    Crafty.load([mapConfig[level].tileset.file].concat(mapConfig[level].files, mapConfig['common'].files), function() {
        Crafty.scene('level_' + level, function() {
            Crafty.background('#000');
            loadMap(level);
            loadSprites();
            loadTobi();
        });
        Crafty.scene('level_' + level);
    });
};

/**
 * Map loader
 */
window.loadMap = function(level) {
    // Calculate the sprite mapping
    var i, spriteMapping = {};
    for (i = 0; i < mapConfig[level].tileset.elements; i++) {
        spriteMapping['m' + level + '_' + (i+1)] = [i % mapConfig[level].tileset.width, Math.floor(i/mapConfig[level].tileset.width)];
    }
    Crafty.sprite(32, mapConfig[level].tileset.file, spriteMapping);
    // spawn the map entities
    var map = MAPS[level],
        compontents, tile;
    for (i = 0; i < map.tiles.length; i++) {
        tile = map.tiles[i];
        compontents = ['2D', config.renderType, 'm' + level + '_' + tile];
        if (mapConfig[level].solids.indexOf(tile) > 0) {
            compontents = compontents.concat(['Solid', 'Collision']);
        }
        CURRENT_MAP.push(Crafty.e(compontents.join(',')).attr({x: i % map.width * 32, y: Math.floor(i/map.width) * 32}));
    }
    log('Map of level ' + level + ' loaded.');
};

/**
 * Our hero :D
 */
window.loadTobi = function() {
    TOBI = Crafty.e('2D, ' + config.renderType + ', tobi, SpriteAnimation, Twoway, Gravity, Collision, Keyboard')
        .attr({x: Crafty.viewport.width / 2, y: Crafty.viewport.height / 2, z: 2, _falling: true})
        .twoway(5, 12)
        .gravity('Solid')
        .gravityConst(0.5)
        .bind('Moved',function(from) {
            if(this.hit('Solid')){
                this.attr({x: from.x, y:from.y});
            }
        })
        .bind('EnterFrame', function() {
            // Camera Stuff
            var vp = {
                x: Crafty.viewport.width/2 - this.x - this.w/2,
                y: Crafty.viewport.height/2 - this.y - this.h/2
            };
            if (vp.x != Crafty.viewport.x){Crafty.viewport.scroll('_x', vp.x);}
            if (vp.y != Crafty.viewport.y){Crafty.viewport.scroll('_y', vp.y);}

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
        })
        .animate('tobi_stand_right', 0, 0, 0)
        .animate('tobi_stand_left', 4, 0, 4)
        .animate('tobi_walk_right', 0, 0, 3)
        .animate('tobi_walk_left', 4, 0, 7)
        .animate('tobi_jump_left', 8, 0, 8)
        .animate('tobi_jump_right', 9, 0, 9)
        .animate('tobi_jump_right', 10, -1);
    //Crafty.viewport.follow(tobi, 300, 300);
};