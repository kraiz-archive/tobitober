var TIME, POINTS, LIVES,
    CURRENT_MAP = [],
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
                Crafty.e(E + 'Image').attr({x: 240, y: 10}).image('gfx/z-crew.png');
            }, 500);
            setTimeout(function() {
                Crafty.e(E + 'Image').attr({x: 190, y: 50}).image('gfx/presents.png');
            }, 1000);
            setTimeout(function() {
                Crafty.e(E + 'Image').attr({x: 170, y: 150}).image('gfx/header.png');
            }, 1500);
            setTimeout(function() {
                Crafty.e(E + 'Image, KeyBoard').attr({x: 242, y: 300}).image('gfx/play.png').bind('KeyDown', function(e) {
                    if (e.keyCode === Crafty.keys['P']) {
                        POINTS = 0;
                        LIVES = 3;
                        TIME = new Date();
                        loadLevel(1);
                    }
                });
                Crafty.e(E + 'Image, KeyBoard').attr({x: 242, y: 350}).image('gfx/exit.png').bind('KeyDown', function(e) {
                    if (e.keyCode === Crafty.keys['E']) {
                        Crafty.scene('menu');
                    }
                });
            }, 2000);
        });
        Crafty.scene('menu');
//        POINTS = 0;
//        LIVES = 3;
//        TIME = new Date();
//        loadLevel(4);
    });

};

/**
 * Loads the common sprites
 */
window.loadSpritesAndSounds = function() {
    for (var sprite in mapConfig['common'].sprites) {
        var value = mapConfig['common'].sprites[sprite];
        Crafty.sprite(value[0], sprite, value[1]);
    }
    for (var sprite in mapConfig['common'].sprites) {

    };
    log('Loaded ' + Object.keys(mapConfig['common'].sprites).length + ' sprites');
    Crafty.audio.add(mapConfig['common'].sounds);
    log('Loaded ' + Object.keys(mapConfig['common'].sounds).length + ' sounds');
};

/**
 * Level loader
 */
window.loadLevel = function(level) {
    log('Loading level ' + level);

    var files = Object.keys(mapConfig['common'].sprites);
    files.concat(mapConfig['common'].files);
    files.concat([mapConfig[level].tileset.file]);
    files.concat(mapConfig[level].files);
    Crafty.load(files, function() {
        Crafty.scene('level_' + level, function() {
            Crafty.background(mapConfig[level].background);
            $('#cr-stage div').css('background', mapConfig[level].background);
            loadSpritesAndSounds();
            loadMap(level);
            loadScoreBoard(level);
            Crafty.audio.play('batman', -1);
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

    var map = MAPS[level],
        element, component;

    // spawn the map entities
    for (i = 0; i < map.tiles.length; i++) {
        element = map.tiles[i];
        if (mapConfig[level].ignore.indexOf(element) < 0) {
            component = Crafty.e(E + 'm' + level + '_' + element).attr({
                x: i % map.width * 32,
                y: Math.floor(i/map.width) * 32
            });
            if (mapConfig[level].solids.indexOf(element) >= 0 ){
                component.requires('Solid, Collision, WiredHitBox').collision();
            }
            CURRENT_MAP.push(component);
        }
    }
    log('Loaded ' + CURRENT_MAP.length + ' level blocks');
    // Load Tobi, NOTE: There's no loop around it, cuz "There can be only one"
    TOBI = Crafty.e(E + 'Tobi').attr({
        x: Crafty.viewport.width/2,
        y: Crafty.viewport.height/2,
        z: 2
    });
    log('Loaded Tobi');
    // onions
    for ( i = 0; i < map.onions.length; i++) {
        element = map.onions[i];
        CURRENT_MAP.push(
            Crafty.e(E + 'Onion').attr({
                x: element[0] * 32,
                y: element[1] * 32,
                z: 2
           })
        );
    }
    log('Loaded ' + i + ' onions');
    // monsters
    for ( i = 0; i < map.monsters.length; i++) {
        element = map.monsters[i];
        var monster =  Crafty.e(E + element[2]).attr({
            x: element[0] * 32,
            y: element[1] * 32,
            z: 2,
        });
        if (config.monsters.runner.types.indexOf(element[2]) >= 0) {
            monster.runner(element[3] > 0);
        }
        if (config.monsters.clompable.indexOf(element[2]) >= 0) {
            monster.requires('Clompable');
        }
        if (config.monsters.follower.types.indexOf(element[2]) >= 0) {
            monster.follow(TOBI);
        }
        CURRENT_MAP.push(monster);
    }
    log('Loaded ' + i + ' monsters');
    log('Map of level ' + level + ' loaded.');
};

/**
 * This funny thing above, where the points are
 */
window.loadScoreBoard = function(level) {
    var followViewport = function() {
        this.x = -Crafty.viewport.x;
        this.y = -Crafty.viewport.y;
    };
    var zeroPrefixed = function(number) {
        return number < 9 ? '0' + number : '' +number;
    };
    Crafty.e(E + 'Image')
          .image('gfx/scoreboard.png')
          .bind('EnterFrame', followViewport);
    Crafty.e('HTML, Time')
          .attr({x:0, y:0, w:640, h:32})
          .bind('EnterFrame', followViewport)
          .bind('EnterFrame', function() {
              var date = new Date(new Date() - TIME);
              $('#time').text(zeroPrefixed(date.getMinutes())+ ':' + zeroPrefixed(date.getSeconds()));
          })
          .replace('<div id="scoreboard">' +
                       '<div id="level">' + mapConfig[level].name + '</div>' +
                       '<div id="points">Punkte: '+ POINTS + '</div>' +
                       '<div id="lives">' + LIVES + '</div>' +
                       '<div id="time">00:00</div>' +
                   '</div>');
};
window.addPoints = function(points) {
    POINTS += points;
    $('#points').text('Punkte: '+ POINTS);
};