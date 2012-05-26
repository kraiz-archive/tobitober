window.log = function() {
    if (config.debug && console != undefined) {
        console.log(arguments);
    }
};



var config = {
   debug: true,
   renderType: 'DOM' // 'Canvas'
};
