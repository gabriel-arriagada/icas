angular.module('AppAdmin').service('CompartirClienteService', function (){
    var cliente;
    return {
        getProperty: function (){
            return cliente;
        },       
        setProperty: function (valor){
            cliente = valor;
        }
    };
});