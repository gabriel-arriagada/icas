(function () {
    'use strict';
    angular.module('ComandasApp', ['ngMaterial', 'ngMessages', 'ngWebSocket']).config(function ($mdThemingProvider, $mdIconProvider) {
        var cyanTextoBlanco = $mdThemingProvider.extendPalette('cyan', {
            'contrastDefaultColor': 'light'
        });
        $mdThemingProvider.definePalette('cyanTextoBlanco', cyanTextoBlanco);
        $mdThemingProvider.theme('default')
                .primaryPalette('cyanTextoBlanco');

        $mdIconProvider
                .icon("comanda", "recursos-admin/assets/svg/ic_comment_24px.svg", 24)
                .icon("mas", "recursos-admin/assets/svg/masGris.svg", 24)
                .icon("cliente", "recursos-admin/assets/svg/ic_person_24px.svg", 24)
                .icon("buscar", "recursos-admin/assets/svg/buscarPedido.svg", 24)
                .icon("notifi", "recursos-admin/assets/svg/ic_notifications_24px.svg", 24)
                .icon("preparado", "recursos-admin/assets/svg/ic_playlist_add_check_24px.svg", 24)
                .icon("entregado", "recursos-admin/assets/svg/ic_thumb_up_24px.svg", 24)
                .icon("formaPago", "recursos-admin/assets/svg/ic_payment_24px.svg", 24)
                .icon("debe", "recursos-admin/assets/svg/ic_attach_money_24px.svg", 24)
                .icon("atras", "recursos-admin/assets/svg/atras_24px.svg", 24)
                .icon("cerrar", "recursos-admin/assets/svg/ic_clear_black_24px.svg", 24)               
                .icon("comentario", "recursos-admin/assets/svg/ic_feedback_24px.svg", 24);
                

    }).directive('detallePedido', function () {
        return {
            restrict: 'E',
            templateUrl: 'recursos-admin/vistas/comandas/detalle-pedido.tmlp.html',
            scope: {
                producto: '@',
                cantidad: '@',
                imagen: '@',
                subTotal: '@'
            }
        };
    }).directive("comentarioPedido", function () {
        return {
            restrict: 'E',
            templateUrl: 'recursos-admin/vistas/comandas/comentario-pedido.tmlp.html',
            scope: {
                comentario: '@'
            }
        };
    });
})();
