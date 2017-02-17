angular.module('AppAdmin').service('CompartirCategoriaService', function() {
    var categoria;
    return {
        getProperty: function() {
            return categoria;
        },
        setProperty: function(value) {
            categoria = value;
        }
    };
});


