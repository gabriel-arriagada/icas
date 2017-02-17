angular.module('AppAdmin').service('CompartirEmpleadoService', function (){
    var empleado;
    return {
        getProperty: function (){
            return empleado;
        },       
        setProperty: function (valor){
            empleado = valor;
        }
    };
});