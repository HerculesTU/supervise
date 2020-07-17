/*
 * Bootstrap Context Menu
 * Author: @sydcanem
 * https://github.com/sydcanem/bootstrap-contextmenu
 *
 * Inspired by Bootstrap's dropdown plugin.
 * Bootstrap (http://getbootstrap.com).
 *
 * Licensed under MIT
 * ========================================================= */
(function(C){var A='[data-toggle="context"]';var B=function(E,D){this.$element=C(E);this.before=D.before||this.before;this.onItem=D.onItem||this.onItem;this.scopes=D.scopes||null;if(D.target){this.$element.data("target",D.target)}this.listen()};B.prototype={constructor:B,show:function(E){var I,F,H,G,D={relatedTarget:this,target:E.currentTarget};if(this.isDisabled()){return}this.closemenu();if(this.before.call(this,E,C(E.currentTarget))===false){return}I=this.getMenu();I.trigger(F=C.Event("show.bs.context",D));H=this.getPosition(E,I);G="li:not(.divider)";I.attr("style","").css(H).addClass("open").on("click.context.data-api",G,C.proxy(this.onItem,this,C(E.currentTarget))).trigger("shown.bs.context",D);C("html").on("click.context.data-api",I.selector,C.proxy(this.closemenu,this));return false},closemenu:function(E){var H,F,G,D;H=this.getMenu();if(!H.hasClass("open")){return}D={relatedTarget:this};H.trigger(F=C.Event("hide.bs.context",D));G="li:not(.divider)";H.removeClass("open").off("click.context.data-api",G).trigger("hidden.bs.context",D);C("html").off("click.context.data-api",H.selector);if(E){E.stopPropagation()}},keydown:function(D){if(D.which==27){this.closemenu(D)}},before:function(D){return true},onItem:function(D){return true},listen:function(){this.$element.on("contextmenu.context.data-api",this.scopes,C.proxy(this.show,this));C("html").on("click.context.data-api",C.proxy(this.closemenu,this));C("html").on("keydown.context.data-api",C.proxy(this.keydown,this))},destroy:function(){this.$element.off(".context.data-api").removeData("context");C("html").off(".context.data-api")},isDisabled:function(){return this.$element.hasClass("disabled")||this.$element.attr("disabled")},getMenu:function(){var D=this.$element.data("target"),E;if(!D){D=this.$element.attr("href");D=D&&D.replace(/.*(?=#[^\s]*$)/,"")}E=C(D);return E&&E.length?E:this.$element.find(D)},getPosition:function(F,E){var N=F.clientX,M=F.clientY,D=C(window).width(),O=C(window).height(),G=E.find(".dropdown-menu").outerWidth(),H=E.find(".dropdown-menu").outerHeight(),K={"position":"absolute","z-index":9999},J,I,L;if(M+H>O){J={"top":M-H+C(window).scrollTop()}}else{J={"top":M+C(window).scrollTop()}}if((N+G>D)&&((N-G)>0)){I={"left":N-G+C(window).scrollLeft()}}else{I={"left":N+C(window).scrollLeft()}}L=E.offsetParent().offset();I.left=I.left-L.left;J.top=J.top-L.top;return C.extend(K,J,I)}};C.fn.contextmenu=function(D,E){return this.each(function(){var F=C(this),H=F.data("context"),G=(typeof D=="object")&&D;if(!H){F.data("context",(H=new B(F,G)))}if(typeof D=="string"){H[D].call(H,E)}})};C.fn.contextmenu.Constructor=B;C(document).on("contextmenu.context.data-api",function(){C(A).each(function(){var D=C(this).data("context");if(!D){return}D.closemenu()})}).on("contextmenu.context.data-api",A,function(D){C(this).contextmenu("show",D);D.preventDefault();D.stopPropagation()})}(jQuery));