(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror"],A)}else{A(CodeMirror)}}})(function(A){A.defineMode("yacas",function(K,D){function O(S){var R={},P=S.split(" ");for(var Q=0;Q<P.length;++Q){R[P[Q]]=true}return R}var H=O("Assert BackQuote D Defun Deriv For ForEach FromFile FromString Function Integrate InverseTaylor Limit LocalSymbols Macro MacroRule MacroRulePattern NIntegrate Rule RulePattern Subst TD TExplicitSum TSum Taylor Taylor1 Taylor2 Taylor3 ToFile ToStdout ToString TraceRule Until While");var F="(?:(?:\\.\\d+|\\d+\\.\\d*|\\d+)(?:[eE][+-]?\\d+)?)";var B="(?:[a-zA-Z\\$'][a-zA-Z0-9\\$']*)";var M=new RegExp(F);var E=new RegExp(B);var L=new RegExp(B+"?_"+B);var C=new RegExp(B+"\\s*\\(");function G(T,S){var R;R=T.next();if(R==='"'){S.tokenize=N;return S.tokenize(T,S)}if(R==="/"){if(T.eat("*")){S.tokenize=I;return S.tokenize(T,S)}if(T.eat("/")){T.skipToEnd();return"comment"}}T.backUp(1);var Q=T.match(/^(\w+)\s*\(/,false);if(Q!==null&&H.hasOwnProperty(Q[1])){S.scopes.push("bodied")}var P=J(S);if(P==="bodied"&&R==="["){S.scopes.pop()}if(R==="["||R==="{"||R==="("){S.scopes.push(R)}P=J(S);if(P==="["&&R==="]"||P==="{"&&R==="}"||P==="("&&R===")"){S.scopes.pop()}if(R===";"){while(P==="bodied"){S.scopes.pop();P=J(S)}}if(T.match(/\d+ *#/,true,false)){return"qualifier"}if(T.match(M,true,false)){return"number"}if(T.match(L,true,false)){return"variable-3"}if(T.match(/(?:\[|\]|{|}|\(|\))/,true,false)){return"bracket"}if(T.match(C,true,false)){T.backUp(1);return"variable"}if(T.match(E,true,false)){return"variable-2"}if(T.match(/(?:\\|\+|\-|\*|\/|,|;|\.|:|@|~|=|>|<|&|\||_|`|'|\^|\?|!|%)/,true,false)){return"operator"}return"error"}function N(T,S){var R,Q=false,P=false;while((R=T.next())!=null){if(R==='"'&&!P){Q=true;break}P=!P&&R==="\\"}if(Q&&!P){S.tokenize=G}return"string"}function I(S,Q){var R,P;while((P=S.next())!=null){if(R==="*"&&P==="/"){Q.tokenize=G;break}R=P}return"comment"}function J(Q){var P=null;if(Q.scopes.length>0){P=Q.scopes[Q.scopes.length-1]}return P}return{startState:function(){return{tokenize:G,scopes:[]}},token:function(Q,P){if(Q.eatSpace()){return null}return P.tokenize(Q,P)},indent:function(R,Q){if(R.tokenize!==G&&R.tokenize!==null){return A.Pass}var P=0;if(Q==="]"||Q==="];"||Q==="}"||Q==="};"||Q===");"){P=-1}return(R.scopes.length+P)*K.indentUnit},electricChars:"{}[]();",blockCommentStart:"/*",blockCommentEnd:"*/",lineComment:"//"}});A.defineMIME("text/x-yacas",{name:"yacas"})});