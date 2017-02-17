angular.module('AppAdmin').controller('CrearCategoriaController', function($mdDialog) {

    /*Los métodos que estan en este controlador se ejecutan desde la plantilla
     * agregar-categoria.template.html y retornan valores a la función que dispara 
     * el dialogo de crear.
     * */
    var vm = this;

    vm.cerrarDialogo = function() {
        $mdDialog.cancel();
    };

    //metodo ejecutado desde agregar-categoria.tenplate.html
    vm.ejecutarAccion = function() {
        //Creo objeto categoria para enviarlo por POST
        var categoria = {categoria: vm.categoria};
        if (vm.categoria != null)
        {
            $mdDialog.hide(categoria);
        }
    };

});

