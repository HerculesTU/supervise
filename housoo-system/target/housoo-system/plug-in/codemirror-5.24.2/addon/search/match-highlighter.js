(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"),require("./matchesonscrollbar"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror","./matchesonscrollbar"],A)}else{A(CodeMirror)}}})(function(K){var D={style:"matchhighlight",minChars:2,delay:100,wordsOnly:false,annotateScrollbar:false,showToken:false,trim:true};function F(M){this.options={};for(var N in D){this.options[N]=(M&&M.hasOwnProperty(N)?M:D)[N]}this.overlay=this.timeout=null;this.matchesonscroll=null;this.active=false}K.defineOption("highlightSelectionMatches",false,function(O,N,M){if(M&&M!=K.Init){C(O);clearTimeout(O.state.matchHighlighter.timeout);O.state.matchHighlighter=null;O.off("cursorActivity",J);O.off("focus",H)}if(N){var P=O.state.matchHighlighter=new F(N);if(O.hasFocus()){P.active=true;I(O)}else{O.on("focus",H)}O.on("cursorActivity",J)}});function J(M){var N=M.state.matchHighlighter;if(N.active||M.hasFocus()){A(M,N)}}function H(M){var N=M.state.matchHighlighter;if(!N.active){N.active=true;A(M,N)}}function A(M,N){clearTimeout(N.timeout);N.timeout=setTimeout(function(){I(M)},N.options.delay)}function G(Q,P,M,O){var R=Q.state.matchHighlighter;Q.addOverlay(R.overlay=B(P,M,O));if(R.options.annotateScrollbar&&Q.showMatchesOnScrollbar){var N=M?new RegExp("\\b"+P+"\\b"):P;R.matchesonscroll=Q.showMatchesOnScrollbar(N,false,{className:"CodeMirror-selection-highlight-scrollbar"})}}function C(M){var N=M.state.matchHighlighter;if(N.overlay){M.removeOverlay(N.overlay);N.overlay=null;if(N.matchesonscroll){N.matchesonscroll.clear();N.matchesonscroll=null}}}function I(M){M.operation(function(){var R=M.state.matchHighlighter;C(M);if(!M.somethingSelected()&&R.options.showToken){var N=R.options.showToken===true?/[\w$]/:R.options.showToken;var U=M.getCursor(),O=M.getLine(U.line),P=U.ch,Q=P;while(P&&N.test(O.charAt(P-1))){--P}while(Q<O.length&&N.test(O.charAt(Q))){++Q}if(P<Q){G(M,O.slice(P,Q),N,R.options.style)}return}var T=M.getCursor("from"),V=M.getCursor("to");if(T.line!=V.line){return}if(R.options.wordsOnly&&!L(M,T,V)){return}var S=M.getRange(T,V);if(R.options.trim){S=S.replace(/^\s+|\s+$/g,"")}if(S.length>=R.options.minChars){G(M,S,false,R.options.style)}})}function L(R,M,N){var P=R.getRange(M,N);if(P.match(/^\w+$/)!==null){if(M.ch>0){var O={line:M.line,ch:M.ch-1};var Q=R.getRange(O,M);if(Q.match(/\W/)===null){return false}}if(N.ch<R.getLine(M.line).length){var O={line:N.line,ch:N.ch+1};var Q=R.getRange(N,O);if(Q.match(/\W/)===null){return false}}return true}else{return false}}function E(N,M){return(!N.start||!M.test(N.string.charAt(N.start-1)))&&(N.pos==N.string.length||!M.test(N.string.charAt(N.pos)))}function B(N,O,M){return{token:function(P){if(P.match(N)&&(!O||E(P,O))){return M}P.next();P.skipTo(N.charAt(0))||P.skipToEnd()}}}});