(function () {
    'use strict';
    $(document).ready(function () {
        /*
         *
         *CONTROL DE TABS*
         *
         */

        $(".tabs li").each(function (index) {
            if (index == 0) {
                $(".slider").css({
                    width: $(this).width() + "px",
                    left: "0px"
                });                
            }
        });

        $(".tabs li").click(function (e) {
            if ($(this).attr('class') != "slider") {
                var anchoLi = $(this).width();
                var position = $(this).position();
                var indice = $(this).index();

                $(".slider").css({
                    left: position.left + "px",
                    width: anchoLi + "px"
                });

                addRipple(this, e);
            }
        });

        /**
         RIPPLE MATERIAL
         */
        function addRipple(element, e) {
            $(".ripple").remove();
            var posX = $(element).offset().left,
                    posY = $(element).offset().top,
                    buttonWidth = $(element).width(),
                    buttonHeight = $(element).height();
            $(element).prepend("<span class='ripple'></span>");
            if (buttonWidth >= buttonHeight) {
                buttonHeight = buttonWidth;
            } else {
                buttonWidth = buttonHeight;
            }
            var x = e.pageX - posX - buttonWidth / 2;
            var y = e.pageY - posY - buttonHeight / 2;
            $(".ripple").css({
                width: buttonWidth,
                height: buttonHeight,
                top: y + 'px',
                left: x + 'px'
            }).addClass("rippleBtn");
        }               
        
    });
})();

