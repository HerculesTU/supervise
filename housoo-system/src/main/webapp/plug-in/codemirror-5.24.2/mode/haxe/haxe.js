(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror"],A)}else{A(CodeMirror)}}})(function(A){A.defineMode("haxe",function(i,R){var e=i.indentUnit;function f(B){return{type:B,style:"keyword"}}var t=f("keyword a"),q=f("keyword b"),r=f("keyword c");var I=f("operator"),O={type:"atom",style:"atom"},Z={type:"attribute",style:"attribute"};var D=f("typedef");var K={"if":t,"while":t,"else":q,"do":q,"try":q,"return":r,"break":r,"continue":r,"new":r,"throw":r,"var":f("var"),"inline":Z,"static":Z,"using":f("import"),"public":Z,"private":Z,"cast":f("cast"),"import":f("import"),"macro":f("macro"),"function":f("function"),"catch":f("catch"),"untyped":f("untyped"),"callback":f("cb"),"for":f("for"),"switch":f("switch"),"case":f("case"),"default":f("default"),"in":I,"never":f("property_access"),"trace":f("trace"),"class":D,"abstract":D,"enum":D,"interface":D,"typedef":D,"extends":D,"implements":D,"dynamic":D,"true":O,"false":O,"null":O};var h=/[+\-*&%=<>!?|]/;function Ag(Al,C,B){C.tokenize=B;return B(Al,C)}function Aj(Am,C){var B=false,Al;while((Al=Am.next())!=null){if(Al==C&&!B){return true}B=!B&&Al=="\\"}}var D,W;function L(Al,C,B){D=Al;W=B;return C}function Ai(An,Al){var C=An.next();if(C=='"'||C=="'"){return Ag(An,Al,v(C))}else{if(/[\[\]{}\(\),;\:\.]/.test(C)){return L(C)}else{if(C=="0"&&An.eat(/x/i)){An.eatWhile(/[\da-f]/i);return L("number","number")}else{if(/\d/.test(C)||C=="-"&&An.eat(/\d/)){An.match(/^\d*(?:\.\d*(?!\.))?(?:[eE][+\-]?\d+)?/);return L("number","number")}else{if(Al.reAllowed&&(C=="~"&&An.eat(/\//))){Aj(An,"/");An.eatWhile(/[gimsu]/);return L("regexp","string-2")}else{if(C=="/"){if(An.eat("*")){return Ag(An,Al,o)}else{if(An.eat("/")){An.skipToEnd();return L("comment","comment")}else{An.eatWhile(h);return L("operator",null,An.current())}}}else{if(C=="#"){An.skipToEnd();return L("conditional","meta")}else{if(C=="@"){An.eat(/:/);An.eatWhile(/[\w_]/);return L("metadata","meta")}else{if(h.test(C)){An.eatWhile(h);return L("operator",null,An.current())}else{var Am;if(/[A-Z]/.test(C)){An.eatWhile(/[\w_<>]/);Am=An.current();return L("type","variable-3",Am)}else{An.eatWhile(/[\w_]/);var Am=An.current(),B=K.propertyIsEnumerable(Am)&&K[Am];return(B&&Al.kwAllowed)?L(B.type,B.style,Am):L("variable","variable",Am)}}}}}}}}}}}function v(B){return function(Al,C){if(Aj(Al,B)){C.tokenize=Ai}return L("string","string")}}function o(Am,C){var Al=false,B;while(B=Am.next()){if(B=="/"&&Al){C.tokenize=Ai;break}Al=(B=="*")}return L("comment","comment")}var Ae={"atom":true,"number":true,"variable":true,"string":true,"regexp":true};function c(Ao,An,B,Al,C,Am){this.indented=Ao;this.column=An;this.type=B;this.prev=C;this.info=Am;if(Al!=null){this.align=Al}}function P(Al,B){for(var C=Al.localVars;C;C=C.next){if(C.name==B){return true}}}function T(Ap,Am,B,An,Al){var C=Ap.cc;w.state=Ap;w.stream=Al;w.marked=null,w.cc=C;if(!Ap.lexical.hasOwnProperty("align")){Ap.lexical.align=true}while(true){var Ao=C.length?C.pop():a;if(Ao(B,An)){while(C.length&&C[C.length-1].lex){C.pop()()}if(w.marked){return w.marked}if(B=="variable"&&P(Ap,An)){return"variable-2"}if(B=="variable"&&X(Ap,An)){return"variable-3"}return Am}}}function X(Am,C){if(/[a-z]/.test(C.charAt(0))){return false}var Al=Am.importedtypes.length;for(var B=0;B<Al;B++){if(Am.importedtypes[B]==C){return true}}}function n(B){var Al=w.state;for(var C=Al.importedtypes;C;C=C.next){if(C.name==B){return}}Al.importedtypes={name:B,next:Al.importedtypes}}var w={state:null,column:null,marked:null,cc:null};function G(){for(var B=arguments.length-1;B>=0;B--){w.cc.push(arguments[B])}}function Ah(){G.apply(null,arguments);return true}function b(B,C){for(var Al=C;Al;Al=Al.next){if(Al.name==B){return true}}return false}function j(B){var C=w.state;if(C.context){w.marked="def";if(b(B,C.localVars)){return}C.localVars={name:B,next:C.localVars}}else{if(C.globalVars){if(b(B,C.globalVars)){return}C.globalVars={name:B,next:C.globalVars}}}}var V={name:"this",next:null};function Ad(){if(!w.state.context){w.state.localVars=V}w.state.context={prev:w.state.context,vars:w.state.localVars}}function x(){w.state.localVars=w.state.context.vars;w.state.context=w.state.context.prev}x.lex=true;function Ac(B,Al){var C=function(){var Am=w.state;Am.lexical=new c(Am.indented,w.stream.column(),B,null,Am.lexical,Al)};C.lex=true;return C}function U(){var B=w.state;if(B.lexical.prev){if(B.lexical.type==")"){B.indented=B.lexical.indented}B.lexical=B.lexical.prev}}U.lex=true;function Ab(B){function C(Al){if(Al==B){return Ah()}else{if(B==";"){return G()}else{return Ah(C)}}}return C}function a(B){if(B=="@"){return Ah(y)}if(B=="var"){return Ah(Ac("vardef"),m,Ab(";"),U)}if(B=="keyword a"){return Ah(Ac("form"),Y,a,U)}if(B=="keyword b"){return Ah(Ac("form"),a,U)}if(B=="{"){return Ah(Ac("}"),Ad,d,U,x)}if(B==";"){return Ah()}if(B=="attribute"){return Ah(u)}if(B=="function"){return Ah(J)}if(B=="for"){return Ah(Ac("form"),Ab("("),Ac(")"),H,Ab(")"),U,a,U)}if(B=="variable"){return Ah(Ac("stat"),E)}if(B=="switch"){return Ah(Ac("form"),Y,Ac("}","switch"),Ab("{"),d,U,U)}if(B=="case"){return Ah(Y,Ab(":"))}if(B=="default"){return Ah(Ab(":"))}if(B=="catch"){return Ah(Ac("form"),Ad,Ab("("),l,Ab(")"),a,U,x)}if(B=="import"){return Ah(M,Ab(";"))}if(B=="typedef"){return Ah(p)}return G(Ac("stat"),Y,Ab(";"),U)}function Y(B){if(Ae.hasOwnProperty(B)){return Ah(Aa)}if(B=="type"){return Ah(Aa)}if(B=="function"){return Ah(J)}if(B=="keyword c"){return Ah(s)}if(B=="("){return Ah(Ac(")"),s,Ab(")"),U,Aa)}if(B=="operator"){return Ah(Y)}if(B=="["){return Ah(Ac("]"),N(s,"]"),U,Aa)}if(B=="{"){return Ah(Ac("}"),N(F,"}"),U,Aa)}return Ah()}function s(B){if(B.match(/[;\}\)\],]/)){return G()}return G(Y)}function Aa(B,C){if(B=="operator"&&/\+\+|--/.test(C)){return Ah(Aa)}if(B=="operator"||B==":"){return Ah(Y)}if(B==";"){return}if(B=="("){return Ah(Ac(")"),N(Y,")"),U,Aa)}if(B=="."){return Ah(z,Aa)}if(B=="["){return Ah(Ac("]"),Y,Ab("]"),U,Aa)}}function u(B){if(B=="attribute"){return Ah(u)}if(B=="function"){return Ah(J)}if(B=="var"){return Ah(m)}}function y(B){if(B==":"){return Ah(y)}if(B=="variable"){return Ah(y)}if(B=="("){return Ah(Ac(")"),N(Q,")"),U,a)}}function Q(B){if(B=="variable"){return Ah()}}function M(B,C){if(B=="variable"&&/[A-Z]/.test(C.charAt(0))){n(C);return Ah()}else{if(B=="variable"||B=="property"||B=="."||C=="*"){return Ah(M)}}}function p(B,C){if(B=="variable"&&/[A-Z]/.test(C.charAt(0))){n(C);return Ah()}else{if(B=="type"&&/[A-Z]/.test(C.charAt(0))){return Ah()}}}function E(B){if(B==":"){return Ah(U,a)}return G(Aa,Ab(";"),U)}function z(B){if(B=="variable"){w.marked="property";return Ah()}}function F(B){if(B=="variable"){w.marked="property"}if(Ae.hasOwnProperty(B)){return Ah(Ab(":"),Y)}}function N(Al,C){function B(Am){if(Am==","){return Ah(Al,B)}if(Am==C){return Ah()}return Ah(Ab(C))}return function(Am){if(Am==C){return Ah()}else{return G(Al,B)}}}function d(B){if(B=="}"){return Ah()}return G(a,d)}function m(B,C){if(B=="variable"){j(C);return Ah(Ak,Af)}return Ah()}function Af(B,C){if(C=="="){return Ah(Y,Af)}if(B==","){return Ah(m)}}function H(B,C){if(B=="variable"){j(C);return Ah(S,Y)}else{return G()}}function S(B,C){if(C=="in"){return Ah()}}function J(B,C){if(B=="variable"||B=="type"){j(C);return Ah(J)}if(C=="new"){return Ah(J)}if(B=="("){return Ah(Ac(")"),Ad,N(l,")"),U,Ak,a,x)}}function Ak(B){if(B==":"){return Ah(k)}}function k(B){if(B=="type"){return Ah()}if(B=="variable"){return Ah()}if(B=="{"){return Ah(Ac("}"),N(g,"}"),U)}}function g(B){if(B=="variable"){return Ah(Ak)}}function l(B,C){if(B=="variable"){j(C);return Ah(Ak)}}return{startState:function(B){var C=["Int","Float","String","Void","Std","Bool","Dynamic","Array"];var Al={tokenize:Ai,reAllowed:true,kwAllowed:true,cc:[],lexical:new c((B||0)-e,0,"block",false),localVars:R.localVars,importedtypes:C,context:R.localVars&&{vars:R.localVars},indented:0};if(R.globalVars&&typeof R.globalVars=="object"){Al.globalVars=R.globalVars}return Al},token:function(Al,C){if(Al.sol()){if(!C.lexical.hasOwnProperty("align")){C.lexical.align=false}C.indented=Al.indentation()}if(Al.eatSpace()){return null}var B=C.tokenize(Al,C);if(D=="comment"){return B}C.reAllowed=!!(D=="operator"||D=="keyword c"||D.match(/^[\[{}\(,;:]$/));C.kwAllowed=D!=".";return T(C,B,D,W,Al)},indent:function(Ao,Al){if(Ao.tokenize!=Ai){return 0}var An=Al&&Al.charAt(0),Am=Ao.lexical;if(Am.type=="stat"&&An=="}"){Am=Am.prev}var B=Am.type,C=An==B;if(B=="vardef"){return Am.indented+4}else{if(B=="form"&&An=="{"){return Am.indented}else{if(B=="stat"||B=="form"){return Am.indented+e}else{if(Am.info=="switch"&&!C){return Am.indented+(/^(?:case|default)\b/.test(Al)?e:2*e)}else{if(Am.align){return Am.column+(C?0:1)}else{return Am.indented+(C?0:e)}}}}}},electricChars:"{}",blockCommentStart:"/*",blockCommentEnd:"*/",lineComment:"//"}});A.defineMIME("text/x-haxe","haxe");A.defineMode("hxml",function(){return{startState:function(){return{define:false,inString:false}},token:function(F,E){var D=F.peek();var B=F.sol();if(D=="#"){F.skipToEnd();return"comment"}if(B&&D=="-"){var C="variable-2";F.eat(/-/);if(F.peek()=="-"){F.eat(/-/);C="keyword a"}if(F.peek()=="D"){F.eat(/[D]/);C="keyword c";E.define=true}F.eatWhile(/[A-Z]/i);return C}var D=F.peek();if(E.inString==false&&D=="'"){E.inString=true;D=F.next()}if(E.inString==true){if(F.skipTo("'")){}else{F.skipToEnd()}if(F.peek()=="'"){F.next();E.inString=false}return"string"}F.next();return null},lineComment:"#"}});A.defineMIME("text/x-hxml","hxml")});