(function() {
    angular.module('AppAdmin')
            .service('VerificarRutService', function() {

                return {
                    verificar: verificar
                };

                function verificar(rut) {
                    var rutCorrecto = true;
                    if (rut != null) {
                        if (/^[0-9]+-[0-9kK]{1}$/.test(rut)) {
                            var numeroMultiplicar = 2;
                            var digito = rut.substr(rut.length - 1, 1);
                            var suma = 0;
                            var digitoCalculado = 0;
                            var posicionAntesDelGuion = rut.length - 3;//posición anterior al guión 7723884-0               
                            for (var i = posicionAntesDelGuion; i >= 0; i--) {
                                suma = suma + (rut[i] * numeroMultiplicar);
                                if (numeroMultiplicar == 7) {
                                    numeroMultiplicar = 2;
                                } else {
                                    numeroMultiplicar++;
                                }
                            }
                            digitoCalculado = 11 - (suma % 11);
                            switch (digitoCalculado) {
                                case 10:
                                    digitoCalculado = "k";
                                    break;
                                case 11:
                                    digitoCalculado = 0;
                                    break;
                            }

                            if (digitoCalculado != digito) {
                                //retorno = 'El rut ingresado no es correcto!';
                                rutCorrecto = false;
                            } else {
                                rutCorrecto = true;
                            }
                        }
                    }
                    return rutCorrecto;
                }

            });
})();
