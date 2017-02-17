angular.module('AppAdmin').service('CompartirVentaService', function (){
    var venta;
    return {
        getProperty: function() {
            return venta;
        },
        setProperty: function(value) {
            venta = value;
        }
    };
});

