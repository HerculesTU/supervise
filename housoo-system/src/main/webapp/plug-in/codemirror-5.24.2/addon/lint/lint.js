(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror"],A)}else{A(CodeMirror)}}})(function(F){var C="CodeMirror-lint-markers";function B(T,W){var V=document.createElement("div");V.className="CodeMirror-lint-tooltip";V.appendChild(W.cloneNode(true));document.body.appendChild(V);function U(X){if(!V.parentNode){return F.off(document,"mousemove",U)}V.style.top=Math.max(0,X.clientY-V.offsetHeight-5)+"px";V.style.left=(X.clientX+5)+"px"}F.on(document,"mousemove",U);U(T);if(V.style.opacity!=null){V.style.opacity=1}return V}function H(T){if(T.parentNode){T.parentNode.removeChild(T)}}function Q(T){if(!T.parentNode){return}if(T.style.opacity==null){H(T)}T.style.opacity=0;setTimeout(function(){H(T)},600)}function G(T,W,U){var V=B(T,W);function Y(){F.off(U,"mouseout",Y);if(V){Q(V);V=null}}var X=setInterval(function(){if(V){for(var Z=U;;Z=Z.parentNode){if(Z&&Z.nodeType==11){Z=Z.host}if(Z==document.body){return}if(!Z){Y();break}}}if(!V){return clearInterval(X)}},400);F.on(U,"mouseout",Y)}function N(V,U,T){this.marked=[];this.options=U;this.timeout=null;this.hasGutter=T;this.onMouseOver=function(W){S(V,W)};this.waitingFor=0}function L(U,T){if(T instanceof Function){return{getAnnotations:T}}if(!T||T===true){T={}}return T}function I(U){var V=U.state.lint;if(V.hasGutter){U.clearGutter(C)}for(var T=0;T<V.marked.length;++T){V.marked[T].clear()}V.marked.length=0}function P(X,V,T,Y){var U=document.createElement("div"),W=U;U.className="CodeMirror-lint-marker-"+V;if(T){W=U.appendChild(document.createElement("div"));W.className="CodeMirror-lint-marker-multiple"}if(Y!=false){F.on(W,"mouseover",function(Z){G(Z,X,W)})}return U}function D(U,T){if(U=="error"){return U}else{return T}}function M(W){var U=[];for(var T=0;T<W.length;++T){var V=W[T],X=V.from.line;(U[X]||(U[X]=[])).push(V)}return U}function O(U){var V=U.severity;if(!V){V="error"}var T=document.createElement("div");T.className="CodeMirror-lint-message-"+V;T.appendChild(document.createTextNode(U.message));return T}function E(X,U,W){var Y=X.state.lint;var V=++Y.waitingFor;function T(){V=-1;X.off("change",T)}X.on("change",T);U(X.getValue(),function(a,Z){X.off("change",T);if(Y.waitingFor!=V){return}if(Z&&a instanceof F){a=Z}A(X,a)},W,X)}function J(W){var X=W.state.lint,U=X.options;var V=U.options||U;var T=U.getAnnotations||W.getHelper(F.Pos(0,0),"lint");if(!T){return}if(U.async||T.async){E(W,T,V)}else{A(W,T(W.getValue(),V,W))}}function A(V,e){I(V);var Y=V.state.lint,Z=Y.options;var T=M(e);for(var U=0;U<T.length;++U){var d=T[U];if(!d){continue}var W=null;var b=Y.hasGutter&&document.createDocumentFragment();for(var a=0;a<d.length;++a){var X=d[a];var c=X.severity;if(!c){c="error"}W=D(W,c);if(Z.formatAnnotation){X=Z.formatAnnotation(X)}if(Y.hasGutter){b.appendChild(O(X))}if(X.to){Y.marked.push(V.markText(X.from,X.to,{className:"CodeMirror-lint-mark-"+c,__annotation:X}))}}if(Y.hasGutter){V.setGutterMarker(U,C,P(b,W,d.length>1,Y.options.tooltips))}}if(Z.onUpdateLinting){Z.onUpdateLinting(e,T,V)}}function K(T){var U=T.state.lint;if(!U){return}clearTimeout(U.timeout);U.timeout=setTimeout(function(){J(T)},U.options.delay||500)}function R(Y,U){var X=U.target||U.srcElement;var V=document.createDocumentFragment();for(var T=0;T<Y.length;T++){var W=Y[T];V.appendChild(O(W))}G(U,V,X)}function S(V,T){var a=T.target||T.srcElement;if(!/\bCodeMirror-lint-mark-/.test(a.className)){return}var W=a.getBoundingClientRect(),b=(W.left+W.right)/2,c=(W.top+W.bottom)/2;var U=V.findMarksAt(V.coordsChar({left:b,top:c},"client"));var X=[];for(var Z=0;Z<U.length;++Z){var Y=U[Z].__annotation;if(Y){X.push(Y)}}if(X.length){R(X,T)}}F.defineOption("lint",false,function(Y,X,V){if(V&&V!=F.Init){I(Y);if(Y.state.lint.options.lintOnChange!==false){Y.off("change",K)}F.off(Y.getWrapperElement(),"mouseover",Y.state.lint.onMouseOver);clearTimeout(Y.state.lint.timeout);delete Y.state.lint}if(X){var W=Y.getOption("gutters"),T=false;for(var U=0;U<W.length;++U){if(W[U]==C){T=true}}var Z=Y.state.lint=new N(Y,L(Y,X),T);if(Z.options.lintOnChange!==false){Y.on("change",K)}if(Z.options.tooltips!=false&&Z.options.tooltips!="gutter"){F.on(Y.getWrapperElement(),"mouseover",Z.onMouseOver)}J(Y)}});F.defineExtension("performLint",function(){if(this.state.lint){J(this)}})});