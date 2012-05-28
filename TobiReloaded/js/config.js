window.log = function() {
    if (config.debug && console != undefined) {
        console.log(arguments.length == 1 ? arguments[0] : arguments);
    }
};

var config = {
   debug: true,
   renderType: Crafty.support.canvas ? 'Canvas' : 'DOM',
   stdComponents: '2D, ' + Crafty.support.canvas ? 'Canvas' : 'DOM',
   stageWidth: 640,
   stageHeight: 480,
   camOffsetX: 100,
   camOffsetY: 100,
   monsters: {
       water: {
           min: 10,
           max: 24
       },
       clompable: ['Schnappi', 'Kroko', 'Nsa'],
       runner: {
           types: ['Schnappi', 'Kroko', 'Nsa', 'Robot', 'Zwiablo'],
           speed: 2
       },
       follower: {
           types: ['Ghost'],
           speed: 1
       }
   }
};

var E = '2D, ' + config.renderType + ', ';

var mapConfig = {
    common: {
        files: ['gfx/scoreboard.png'],
        sprites: {
            'gfx/tobi.png': [32, {
                spriteTobi: [0, 0],
            }],
            'gfx/zwiebel.png': [32, {
                spriteOnion: [0, 0],
            }],
            'gfx/robot.png': [32, {
                spriteRobot: [0, 0],
            }],
            'gfx/schnappi.png': [32, {
                spriteSchnappi: [0, 0],
            }],
            'gfx/wasser.png': [32, {
                spriteWater: [0, 0],
            }],
            'gfx/wasserviech.png': [32, {
                spriteKroko: [0, 0],
            }],
            'gfx/nsa.png': [32, {
                spriteNsa: [0, 0],
            }],
            'gfx/geist.png': [32, {
                spriteGhost: [0, 0],
            }],
            'gfx/zwiebli.png': [32, {
                spriteZwiebli: [0, 0],
            }],
            'gfx/zwiablo.png': [128, {
                spriteZwiablo: [0, 0],
            }]
        },
        sounds: {
            batman: ['sfx/batman.mp3', 'sfx/batman.ogg'],
            onion: ['sfx/zwiebel.mp3', 'sfx/zwiebel.ogg'],
            schnappi: ['sfx/schnappi.mp3', 'sfx/schnappi.ogg'],
            kroko: ['sfx/wasserviech.mp3', 'sfx/wasserviech.ogg'],
            nsa: ['sfx/nsa.mp3', 'sfx/nsa.ogg'],
            jump: ['sfx/tobi_jump.mp3', 'sfx/tobi_jump.ogg'],
            dead: ['sfx/tobi_tot.mp3', 'sfx/tobi_tot.ogg']
        }
    },
    1: {
        name: 'Strand Hawaii',
        background: '#000',
        tileset: {
            file: 'gfx/tileset_1.png',
            width: 10,
            elements: 29
        },
        ignore: [1],
        solids: [4, 6, 7, 9, 10, 11, 12, 21, 22, 23, 24, 25, 26, 27],
        files: ['gfx/schnappi.png']
    },
    2: {
        name: 'Kreuzfahrtschiff',
        background: '#000',
        tileset: {
            file: 'gfx/tileset_2.png',
            width: 9,
            elements: 35
        },
        ignore: [1],
        solids: [8, 9, 10, 18, 29, 30],
        files: ['gfx/schnappi.png']
    },
    3: {
        name: 'Area51 Bunker',
        background: 'url(gfx/bg3.png)',
        tileset: {
            file: 'gfx/tileset_3.png',
            width: 8,
            elements: 32
        },
        ignore: [21],
        solids: [19, 20, 22, 28, 29, 31],
        files: ['gfx/bg3.png', 'gfx/schnappi.png']
    },
    4: {
        name: 'Märzdorf',
        background: 'url(gfx/background_map_4.png)',
        tileset: {
            file: 'gfx/tileset_4.png',
            width: 7,
            elements: 6
        },
        ignore: [1],
        solids: [2, 5],
        files: ['gfx/schnappi.png']
    }
};
