/*
 * Main menu on load
 */
window.onload = function() {
    Crafty.init(640, 480);
    Crafty.load(['gfx/play.png', 'gfx/presents.png', 'gfx/exit.png', 'gfx/z-crew.png', 'gfx/header.png'], function() {
        Crafty.scene('menu', function() {
            Crafty.background('#000');
            setTimeout(function() {
                Crafty.e("2D, DOM, Image").attr({x: 240, y: 10}).image('gfx/z-crew.png');
            }, 500);
            setTimeout(function() {
                Crafty.e("2D, DOM, Image").attr({x: 190, y: 50}).image('gfx/presents.png');
            }, 1000);
            setTimeout(function() {
                Crafty.e("2D, DOM, Image").attr({x: 170, y: 150}).image('gfx/header.png');
            }, 1500);
            setTimeout(function() {
                Crafty.e("2D, DOM, Image, KeyBoard, Mouse").attr({x: 242, y: 300}).image('gfx/play.png').bind('KeyDown', function(e) {
                    if (e.keyCode === Crafty.keys['P']) {
                        loadLevel(1);
                    }
                });
                Crafty.e("2D, DOM, Image").attr({x: 242, y: 350}).image('gfx/exit.png').requires('KeyBoard').bind('KeyDown', function(e) {
                    if (e.keyCode === Crafty.keys['E']) {
                        Crafty.scene("menu");
                    }
                });
            }, 2000);
        });
        Crafty.scene("menu");
    });

};

/*
 * Level loader
 */
window.loadLevel = function(level) {
    Crafty.load([mapConfig[level].map_file, mapConfig[level].map_tileset], function() {

    });
};
