(function () {
    'use strict';
    angular.module('ComandasApp')
            .directive('getColor', function () {
                var colores = [
                    "#00796B",
                    "#009688",
                    "#0097A7",
                    "#00BCD4",
                    "#03A9F4",
                    "#C2185B",
                    "#E91E63",
                    "#9C27B0",
                    "#AFB42B",
                    "#689F38"
                ];

                return {
                    restrict: "A",
                    scope: {
                        attr: "@attr"
                    },
                    link: function (scope, element, atributo) {
                        var estilo = "background-color: " + getColor() + ";";
                        atributo.$set(scope.attr || "style", estilo);
                    }
                };

                function getColor() {
                    var indice = Math.floor(Math.random() * (colores.length - 1)) + 0;
                    return colores[indice];
                }

            });
})();