(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror"],A)}else{A(CodeMirror)}}})(function(B){B.defineOption("rulers",false,function(D,C){if(D.state.rulerDiv){D.state.rulerDiv.parentElement.removeChild(D.state.rulerDiv);D.state.rulerDiv=null;D.off("refresh",A)}if(C&&C.length){D.state.rulerDiv=D.display.lineSpace.parentElement.insertBefore(document.createElement("div"),D.display.lineSpace);D.state.rulerDiv.className="CodeMirror-rulers";A(D);D.on("refresh",A)}});function A(C){C.state.rulerDiv.textContent="";var E=C.getOption("rulers");var G=C.defaultCharWidth();var D=C.charCoords(B.Pos(C.firstLine(),0),"div").left;C.state.rulerDiv.style.minHeight=(C.display.scroller.offsetHeight+30)+"px";for(var F=0;F<E.length;F++){var H=document.createElement("div");H.className="CodeMirror-ruler";var J,I=E[F];if(typeof I=="number"){J=I}else{J=I.column;if(I.className){H.className+=" "+I.className}if(I.color){H.style.borderColor=I.color}if(I.lineStyle){H.style.borderLeftStyle=I.lineStyle}if(I.width){H.style.borderLeftWidth=I.width}}H.style.left=(D+J*G)+"px";C.state.rulerDiv.appendChild(H)}}});