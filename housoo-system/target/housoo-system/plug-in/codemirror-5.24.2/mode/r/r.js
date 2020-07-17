(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror"],A)}else{A(CodeMirror)}}})(function(A){A.registerHelper("wordChars","r",/[\w.]/);A.defineMode("r",function(B){function I(Q){var N=Q.split(" "),P={};for(var O=0;O<N.length;++O){P[N[O]]=true}return P}var L=I("NULL NA Inf NaN NA_integer_ NA_real_ NA_complex_ NA_character_");var D=I("list quote bquote eval return call parse deparse");var F=I("if else repeat while function for in next break");var E=I("if else repeat while function for");var J=/[+\-*\/^<>=!&|~$:]/;var H;function G(Q,O){H=null;var N=Q.next();if(N=="#"){Q.skipToEnd();return"comment"}else{if(N=="0"&&Q.eat("x")){Q.eatWhile(/[\da-f]/i);return"number"}else{if(N=="."&&Q.eat(/\d/)){Q.match(/\d*(?:e[+\-]?\d+)?/);return"number"}else{if(/\d/.test(N)){Q.match(/\d*(?:\.\d+)?(?:e[+\-]\d+)?L?/);return"number"}else{if(N=="'"||N=='"'){O.tokenize=K(N);return"string"}else{if(N=="`"){Q.match(/[^`]+`/);return"variable-3"}else{if(N=="."&&Q.match(/.[.\d]+/)){return"keyword"}else{if(/[\w\.]/.test(N)&&N!="_"){Q.eatWhile(/[\w\.]/);var P=Q.current();if(L.propertyIsEnumerable(P)){return"atom"}if(F.propertyIsEnumerable(P)){if(E.propertyIsEnumerable(P)&&!Q.match(/\s*if(\s+|$)/,false)){H="block"}return"keyword"}if(D.propertyIsEnumerable(P)){return"builtin"}return"variable"}else{if(N=="%"){if(Q.skipTo("%")){Q.next()}return"operator variable-2"}else{if((N=="<"&&Q.eat("-"))||(N=="<"&&Q.match("<-"))||(N=="-"&&Q.match(/>>?/))){return"operator arrow"}else{if(N=="="&&O.ctx.argList){return"arg-is"}else{if(J.test(N)){if(N=="$"){return"operator dollar"}Q.eatWhile(J);return"operator"}else{if(/[\(\){}\[\];]/.test(N)){H=N;if(N==";"){return"semi"}return null}else{return null}}}}}}}}}}}}}}function K(N){return function(R,Q){if(R.eat("\\")){var P=R.next();if(P=="x"){R.match(/^[a-f0-9]{2}/i)}else{if((P=="u"||P=="U")&&R.eat("{")&&R.skipTo("}")){R.next()}else{if(P=="u"){R.match(/^[a-f0-9]{4}/i)}else{if(P=="U"){R.match(/^[a-f0-9]{8}/i)}else{if(/[0-7]/.test(P)){R.match(/^[0-7]{1,2}/)}}}}}return"string-2"}else{var O;while((O=R.next())!=null){if(O==N){Q.tokenize=G;break}if(O=="\\"){R.backUp(1);break}}return"string"}}}function M(P,N,O){P.ctx={type:N,indent:P.indent,align:null,column:O.column(),prev:P.ctx}}function C(N){N.indent=N.ctx.indent;N.ctx=N.ctx.prev}return{startState:function(){return{tokenize:G,ctx:{type:"top",indent:-B.indentUnit,align:false},indent:0,afterIdent:false}},token:function(Q,O){if(Q.sol()){if(O.ctx.align==null){O.ctx.align=false}O.indent=Q.indentation()}if(Q.eatSpace()){return null}var N=O.tokenize(Q,O);if(N!="comment"&&O.ctx.align==null){O.ctx.align=true}var P=O.ctx.type;if((H==";"||H=="{"||H=="}")&&P=="block"){C(O)}if(H=="{"){M(O,"}",Q)}else{if(H=="("){M(O,")",Q);if(O.afterIdent){O.ctx.argList=true}}else{if(H=="["){M(O,"]",Q)}else{if(H=="block"){M(O,"block",Q)}else{if(H==P){C(O)}}}}}O.afterIdent=N=="variable"||N=="keyword";return N},indent:function(R,O){if(R.tokenize!=G){return 0}var Q=O&&O.charAt(0),N=R.ctx,P=Q==N.type;if(N.type=="block"){return N.indent+(Q=="{"?0:B.indentUnit)}else{if(N.align){return N.column+(P?0:1)}else{return N.indent+(P?0:B.indentUnit)}}},lineComment:"#"}});A.defineMIME("text/x-rsrc","r")});