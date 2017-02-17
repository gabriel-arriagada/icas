(function () {
    'use strict';
    angular.module('LogInApp', ['ngMaterial', 'ngMessages'])
            .config(function ($mdThemingProvider, $mdIconProvider) {

                $mdThemingProvider.theme('default')
                        .primaryPalette('blue')
                        .accentPalette('orange')
                        .warnPalette('red');

                $mdIconProvider
                        .icon("usuario", "../icas-admin/svg/ic_account_circle_24px.svg", 24)
                        .icon("cerrar", "../icas-admin/svg/ic_close_24px.svg", 24);
            });
})();
