(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror"],A)}else{A(CodeMirror)}}})(function(A){A.defineMode("jinja2",function(){var F=["and","as","block","endblock","by","cycle","debug","else","elif","extends","filter","endfilter","firstof","for","endfor","if","endif","ifchanged","endifchanged","ifequal","endifequal","ifnotequal","endifnotequal","in","include","load","not","now","or","parsed","regroup","reversed","spaceless","endspaceless","ssi","templatetag","openblock","closeblock","openvariable","closevariable","openbrace","closebrace","opencomment","closecomment","widthratio","url","with","endwith","get_current_language","trans","endtrans","noop","blocktrans","endblocktrans","get_available_languages","get_current_language_bidi","plural"],G=/^[+\-*&%=<>!?|~^]/,C=/^[:\[\(\{]/,E=["true","false"],D=/^(\d[+\-\*\/])?\d+(\.\d+)?/;F=new RegExp("(("+F.join(")|(")+"))\\b");E=new RegExp("(("+E.join(")|(")+"))\\b");function B(J,I){var H=J.peek();if(I.incomment){if(!J.skipTo("#}")){J.skipToEnd()}else{J.eatWhile(/\#|}/);I.incomment=false}return"comment"}else{if(I.intag){if(I.operator){I.operator=false;if(J.match(E)){return"atom"}if(J.match(D)){return"number"}}if(I.sign){I.sign=false;if(J.match(E)){return"atom"}if(J.match(D)){return"number"}}if(I.instring){if(H==I.instring){I.instring=false}J.next();return"string"}else{if(H=="'"||H=='"'){I.instring=H;J.next();return"string"}else{if(J.match(I.intag+"}")||J.eat("-")&&J.match(I.intag+"}")){I.intag=false;return"tag"}else{if(J.match(G)){I.operator=true;return"operator"}else{if(J.match(C)){I.sign=true}else{if(J.eat(" ")||J.sol()){if(J.match(F)){return"keyword"}if(J.match(E)){return"atom"}if(J.match(D)){return"number"}if(J.sol()){J.next()}}else{J.next()}}}}}}return"variable"}else{if(J.eat("{")){if(H=J.eat("#")){I.incomment=true;if(!J.skipTo("#}")){J.skipToEnd()}else{J.eatWhile(/\#|}/);I.incomment=false}return"comment"}else{if(H=J.eat(/\{|%/)){I.intag=H;if(H=="{"){I.intag="}"}J.eat("-");return"tag"}}}}}J.next()}return{startState:function(){return{tokenize:B}},token:function(I,H){return H.tokenize(I,H)}}})});