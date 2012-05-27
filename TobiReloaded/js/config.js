window.log = function() {
    if (config.debug && console != undefined) {
        console.log(arguments.length == 1 ? arguments[0] : arguments);
    }
};

var config = {
   debug: true,
   renderType: 'Canvas', // '',
   stageWidth: 640,
   stageHeight: 480,
   camOffsetX: 100,
   camOffsetY: 100
};

var mapConfig = {
    common: {
        files: ['sfx/batman.mp3', 'sfx/tobi_jump.mp3', 'sfx/tobi_tot.mp3', 'sfx/zwiebel.mp3',
                'gfx/info_msg.png', 'gfx/scoreboard.png', 'gfx/tobi.png', 'gfx/zwiebel.png'],
        sprites: {
            'gfx/tobi.png': {
                tobi: [0, 0],
            },
            'gfx/zwiebel.png': {
                onion: [0, 0],
            },
            'gfx/robot.png': {
                robot: [0, 0],
            },
            'gfx/schnappi.png': {
                schnappi: [0, 0],
            },
            'gfx/wasser.png': {
                water: [0, 0],
            },
            'gfx/wasserviech.png': {
                kroko: [0, 0],
            },
            'gfx/geist.png': {
                ghost: [0, 0],
            }
        }
    },
    1: {
        tileset: {
            file: 'gfx/tileset_1.png',
            width: 10,
            elements: 29
        },
        solids: [4, 6, 7, 9, 10, 11, 12, 21, 22, 23, 24, 25, 26, 27],
        files: ['sfx/schnappi.mp3', 'gfx/schnappi.png']
    },
    2: {
        tileset: {
            file: 'gfx/tileset_2.png',
            width: 9,
            elements: 35
        },
        solids: [8, 9, 10, 18, 29, 30],
        files: ['sfx/schnappi.mp3', 'gfx/schnappi.png']
    },
    3: {
        tileset: {
            file: 'gfx/tileset_3.png',
            width: 8,
            elements: 32
        },
        solids: [19, 20, 22, 28, 29, 31],
        files: ['sfx/schnappi.mp3', 'gfx/schnappi.png']
    },
    4: {
        tileset: {
            file: 'gfx/tileset_4.png',
            width: 7,
            elements: 6
        },
        solids: [2, 5],
        files: ['sfx/schnappi.mp3', 'gfx/schnappi.png']
    }
};
