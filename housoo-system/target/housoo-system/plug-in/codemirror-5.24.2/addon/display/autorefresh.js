(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror"],A)}else{A(CodeMirror)}}})(function(B){B.defineOption("autoRefresh",false,function(E,D){if(E.state.autoRefresh){C(E,E.state.autoRefresh);E.state.autoRefresh=null}if(D&&E.display.wrapper.offsetHeight==0){A(E,E.state.autoRefresh={delay:D.delay||250})}});function A(E,F){function D(){if(E.display.wrapper.offsetHeight){C(E,F);if(E.display.lastWrapHeight!=E.display.wrapper.clientHeight){E.refresh()}}else{F.timeout=setTimeout(D,F.delay)}}F.timeout=setTimeout(D,F.delay);F.hurry=function(){clearTimeout(F.timeout);F.timeout=setTimeout(D,50)};B.on(window,"mouseup",F.hurry);B.on(window,"keyup",F.hurry)}function C(D,E){clearTimeout(E.timeout);B.off(window,"mouseup",E.hurry);B.off(window,"keyup",E.hurry)}});