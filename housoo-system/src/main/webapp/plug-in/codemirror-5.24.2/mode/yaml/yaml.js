(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror"],A)}else{A(CodeMirror)}}})(function(A){A.defineMode("yaml",function(){var C=["true","false","on","off","yes","no"];var B=new RegExp("\\b(("+C.join(")|(")+"))$","i");return{token:function(G,F){var E=G.peek();var D=F.escaped;F.escaped=false;if(E=="#"&&(G.pos==0||/\s/.test(G.string.charAt(G.pos-1)))){G.skipToEnd();return"comment"}if(G.match(/^('([^']|\\.)*'?|"([^"]|\\.)*"?)/)){return"string"}if(F.literal&&G.indentation()>F.keyCol){G.skipToEnd();return"string"}else{if(F.literal){F.literal=false}}if(G.sol()){F.keyCol=0;F.pair=false;F.pairStart=false;if(G.match(/---/)){return"def"}if(G.match(/\.\.\./)){return"def"}if(G.match(/\s*-\s+/)){return"meta"}}if(G.match(/^(\{|\}|\[|\])/)){if(E=="{"){F.inlinePairs++}else{if(E=="}"){F.inlinePairs--}else{if(E=="["){F.inlineList++}else{F.inlineList--}}}return"meta"}if(F.inlineList>0&&!D&&E==","){G.next();return"meta"}if(F.inlinePairs>0&&!D&&E==","){F.keyCol=0;F.pair=false;F.pairStart=false;G.next();return"meta"}if(F.pairStart){if(G.match(/^\s*(\||\>)\s*/)){F.literal=true;return"meta"}if(G.match(/^\s*(\&|\*)[a-z0-9\._-]+\b/i)){return"variable-2"}if(F.inlinePairs==0&&G.match(/^\s*-?[0-9\.\,]+\s?$/)){return"number"}if(F.inlinePairs>0&&G.match(/^\s*-?[0-9\.\,]+\s?(?=(,|}))/)){return"number"}if(G.match(B)){return"keyword"}}if(!F.pair&&G.match(/^\s*(?:[,\[\]{}&*!|>'"%@`][^\s'":]|[^,\[\]{}#&*!|>'"%@`])[^#]*?(?=\s*:($|\s))/)){F.pair=true;F.keyCol=G.indentation();return"atom"}if(F.pair&&G.match(/^:\s*/)){F.pairStart=true;return"meta"}F.pairStart=false;F.escaped=(E=="\\");G.next();return null},startState:function(){return{pair:false,pairStart:false,keyCol:0,inlinePairs:0,inlineList:0,literal:false,escaped:false}}}});A.defineMIME("text/x-yaml","yaml");A.defineMIME("text/yaml","yaml")});