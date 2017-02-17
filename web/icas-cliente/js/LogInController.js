(function() {
    angular.module('LogInApp').controller('LogInController', function(IrAlCarrito) {
        var vm = this;
        vm.irAlCarrito = IrAlCarrito.getIr();        
    });
})();