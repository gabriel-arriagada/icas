angular.module('AppAdmin').service('CompartirRutService', function (){
    var rut;
    return {
        getProperty: function() {
            return rut;
        },
        setProperty: function(value) {
            rut = value;
        }
    } 
});