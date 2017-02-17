angular.module('AppAdmin').service('CompartirProductoService', function() {
    var producto;
    return {
        getProperty: function() {
            return producto;
        },
        setProperty: function(value) {
            producto = value;
        }
    };

});
