(function(D){function A(E,F){return(typeof E=="function")?(E.call(F)):E}function B(E){while(E=E.parentNode){if(E==document){return true}}return false}function C(F,E){this.$element=D(F);this.options=E;this.enabled=true;this.fixTitle()}C.prototype={show:function(){var H=this.getTitle();if(H&&this.enabled){var K=this.tip();K.find(".tipsy-inner")[this.options.html?"html":"text"](H);K[0].className="tipsy";K.remove().css({top:0,left:0,visibility:"hidden",display:"block"}).prependTo(document.body);var G=D.extend({},this.$element.offset(),{width:this.$element[0].offsetWidth,height:this.$element[0].offsetHeight});var I=K[0].offsetWidth,J=K[0].offsetHeight,E=A(this.options.gravity,this.$element[0]);var F;switch(E.charAt(0)){case"n":F={top:G.top+G.height+this.options.offset,left:G.left+G.width/2-I/2};break;case"s":F={top:G.top-J-this.options.offset,left:G.left+G.width/2-I/2};break;case"e":F={top:G.top+G.height/2-J/2,left:G.left-I-this.options.offset};break;case"w":F={top:G.top+G.height/2-J/2,left:G.left+G.width+this.options.offset};break}if(E.length==2){if(E.charAt(1)=="w"){F.left=G.left+G.width/2-15}else{F.left=G.left+G.width/2-I+15}}K.css(F).addClass("tipsy-"+E);K.find(".tipsy-arrow")[0].className="tipsy-arrow tipsy-arrow-"+E.charAt(0);if(this.options.className){K.addClass(A(this.options.className,this.$element[0]))}if(this.options.fade){K.stop().css({opacity:0,display:"block",visibility:"visible"}).animate({opacity:this.options.opacity})}else{K.css({visibility:"visible",opacity:this.options.opacity})}}},hide:function(){if(this.options.fade){this.tip().stop().fadeOut(function(){D(this).remove()})}else{this.tip().remove()}},fixTitle:function(){var E=this.$element;if(E.attr("title")||typeof(E.attr("original-title"))!="string"){E.attr("original-title",E.attr("title")||"").removeAttr("title")}},getTitle:function(){var F,G=this.$element,E=this.options;this.fixTitle();var F,E=this.options;if(typeof E.title=="string"){F=G.attr(E.title=="title"?"original-title":E.title)}else{if(typeof E.title=="function"){F=E.title.call(G[0])}}F=(""+F).replace(/(^\s*|\s*$)/,"");return F||E.fallback},tip:function(){if(!this.$tip){this.$tip=D('<div class="tipsy"></div>').html('<div class="tipsy-arrow"></div><div class="tipsy-inner"></div>');this.$tip.data("tipsy-pointee",this.$element[0])}return this.$tip},validate:function(){if(!this.$element[0].parentNode){this.hide();this.$element=null;this.options=null}},enable:function(){this.enabled=true},disable:function(){this.enabled=false},toggleEnabled:function(){this.enabled=!this.enabled}};D.fn.tipsy=function(G){if(G===true){return this.data("tipsy")}else{if(typeof G=="string"){var K=this.data("tipsy");if(K){K[G]()}return this}}G=D.extend({},D.fn.tipsy.defaults,G);function F(N){var M=D.data(N,"tipsy");if(!M){M=new C(N,D.fn.tipsy.elementOptions(N,G));D.data(N,"tipsy",M)}return M}function J(){var M=F(this);M.hoverState="in";if(G.delayIn==0){M.show()}else{M.fixTitle();setTimeout(function(){if(M.hoverState=="in"){M.show()}},G.delayIn)}}function I(){var M=F(this);M.hoverState="out";if(G.delayOut==0){M.hide()}else{setTimeout(function(){if(M.hoverState=="out"){M.hide()}},G.delayOut)}}if(!G.live){this.each(function(){F(this)})}if(G.trigger!="manual"){var H=G.live?"live":"bind",L=G.trigger=="hover"?"mouseenter":"focus",E=G.trigger=="hover"?"mouseleave":"blur";this[H](L,J)[H](E,I)}return this};D.fn.tipsy.defaults={className:null,delayIn:0,delayOut:0,fade:false,fallback:"",gravity:"n",html:false,live:false,offset:0,opacity:0.8,title:"title",trigger:"hover"};D.fn.tipsy.revalidate=function(){D(".tipsy").each(function(){var E=D.data(this,"tipsy-pointee");if(!E||!B(E)){D(this).remove()}})};D.fn.tipsy.elementOptions=function(F,E){return D.metadata?D.extend({},E,D(F).metadata()):E};D.fn.tipsy.autoNS=function(){return D(this).offset().top>(D(document).scrollTop()+D(window).height()/2)?"s":"n"};D.fn.tipsy.autoWE=function(){return D(this).offset().left>(D(document).scrollLeft()+D(window).width()/2)?"e":"w"};D.fn.tipsy.autoBounds=function(E,F){return function(){var G={ns:F[0],ew:(F.length>1?F[1]:false)},J=D(document).scrollTop()+E,H=D(document).scrollLeft()+E,I=D(this);if(I.offset().top<J){G.ns="n"}if(I.offset().left<H){G.ew="w"}if(D(window).width()+D(document).scrollLeft()-I.offset().left<E){G.ew="e"}if(D(window).height()+D(document).scrollTop()-I.offset().top<E){G.ns="s"}return G.ns+(G.ew?G.ew:"")}}})(jQuery);