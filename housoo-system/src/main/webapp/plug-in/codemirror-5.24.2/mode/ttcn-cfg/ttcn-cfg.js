(function(A){if(typeof exports=="object"&&typeof module=="object"){A(require("../../lib/codemirror"))}else{if(typeof define=="function"&&define.amd){define(["../../lib/codemirror"],A)}else{A(CodeMirror)}}})(function(B){B.defineMode("ttcn-cfg",function(D,K){var E=D.indentUnit,G=K.keywords||{},H=K.fileNCtrlMaskOptions||{},J=K.externalCommands||{},P=K.multiLineStrings,C=K.indentStatements!==false;var Q=/[\|]/;var N;function M(U,T){var S=U.next();if(S=='"'||S=="'"){T.tokenize=O(S);return T.tokenize(U,T)}if(/[:=]/.test(S)){N=S;return"punctuation"}if(S=="#"){U.skipToEnd();return"comment"}if(/\d/.test(S)){U.eatWhile(/[\w\.]/);return"number"}if(Q.test(S)){U.eatWhile(Q);return"operator"}if(S=="["){U.eatWhile(/[\w_\]]/);return"number sectionTitle"}U.eatWhile(/[\w\$_]/);var R=U.current();if(G.propertyIsEnumerable(R)){return"keyword"}if(H.propertyIsEnumerable(R)){return"negative fileNCtrlMaskOptions"}if(J.propertyIsEnumerable(R)){return"negative externalCommands"}return"variable"}function O(R){return function(X,V){var T=false,W,S=false;while((W=X.next())!=null){if(W==R&&!T){var U=X.peek();if(U){U=U.toLowerCase();if(U=="b"||U=="h"||U=="o"){X.next()}}S=true;break}T=!T&&W=="\\"}if(S||!(T||P)){V.tokenize=null}return"string"}}function F(V,U,R,T,S){this.indented=V;this.column=U;this.type=R;this.align=T;this.prev=S}function L(U,S,R){var T=U.indented;if(U.context&&U.context.type=="statement"){T=U.context.indented}return U.context=new F(T,S,R,null,U.context)}function I(S){var R=S.context.type;if(R==")"||R=="]"||R=="}"){S.indented=S.context.indented}return S.context=S.context.prev}return{startState:function(R){return{tokenize:null,context:new F((R||0)-E,0,"top",false),indented:0,startOfLine:true}},token:function(U,S){var T=S.context;if(U.sol()){if(T.align==null){T.align=false}S.indented=U.indentation();S.startOfLine=true}if(U.eatSpace()){return null}N=null;var R=(S.tokenize||M)(U,S);if(R=="comment"){return R}if(T.align==null){T.align=true}if((N==";"||N==":"||N==",")&&T.type=="statement"){I(S)}else{if(N=="{"){L(S,U.column(),"}")}else{if(N=="["){L(S,U.column(),"]")}else{if(N=="("){L(S,U.column(),")")}else{if(N=="}"){while(T.type=="statement"){T=I(S)}if(T.type=="}"){T=I(S)}while(T.type=="statement"){T=I(S)}}else{if(N==T.type){I(S)}else{if(C&&(((T.type=="}"||T.type=="top")&&N!=";")||(T.type=="statement"&&N=="newstatement"))){L(S,U.column(),"statement")}}}}}}}S.startOfLine=false;return R},electricChars:"{}",lineComment:"#",fold:"brace"}});function A(F){var E={},C=F.split(" ");for(var D=0;D<C.length;++D){E[C[D]]=true}return E}B.defineMIME("text/x-ttcn-cfg",{name:"ttcn-cfg",keywords:A("Yes No LogFile FileMask ConsoleMask AppendFile TimeStampFormat LogEventTypes SourceInfoFormat LogEntityName LogSourceInfo DiskFullAction LogFileNumber LogFileSize MatchingHints Detailed Compact SubCategories Stack Single None Seconds DateTime Time Stop Error Retry Delete TCPPort KillTimer NumHCs UnixSocketsEnabled LocalAddress"),fileNCtrlMaskOptions:A("TTCN_EXECUTOR TTCN_ERROR TTCN_WARNING TTCN_PORTEVENT TTCN_TIMEROP TTCN_VERDICTOP TTCN_DEFAULTOP TTCN_TESTCASE TTCN_ACTION TTCN_USER TTCN_FUNCTION TTCN_STATISTICS TTCN_PARALLEL TTCN_MATCHING TTCN_DEBUG EXECUTOR ERROR WARNING PORTEVENT TIMEROP VERDICTOP DEFAULTOP TESTCASE ACTION USER FUNCTION STATISTICS PARALLEL MATCHING DEBUG LOG_ALL LOG_NOTHING ACTION_UNQUALIFIED DEBUG_ENCDEC DEBUG_TESTPORT DEBUG_UNQUALIFIED DEFAULTOP_ACTIVATE DEFAULTOP_DEACTIVATE DEFAULTOP_EXIT DEFAULTOP_UNQUALIFIED ERROR_UNQUALIFIED EXECUTOR_COMPONENT EXECUTOR_CONFIGDATA EXECUTOR_EXTCOMMAND EXECUTOR_LOGOPTIONS EXECUTOR_RUNTIME EXECUTOR_UNQUALIFIED FUNCTION_RND FUNCTION_UNQUALIFIED MATCHING_DONE MATCHING_MCSUCCESS MATCHING_MCUNSUCC MATCHING_MMSUCCESS MATCHING_MMUNSUCC MATCHING_PCSUCCESS MATCHING_PCUNSUCC MATCHING_PMSUCCESS MATCHING_PMUNSUCC MATCHING_PROBLEM MATCHING_TIMEOUT MATCHING_UNQUALIFIED PARALLEL_PORTCONN PARALLEL_PORTMAP PARALLEL_PTC PARALLEL_UNQUALIFIED PORTEVENT_DUALRECV PORTEVENT_DUALSEND PORTEVENT_MCRECV PORTEVENT_MCSEND PORTEVENT_MMRECV PORTEVENT_MMSEND PORTEVENT_MQUEUE PORTEVENT_PCIN PORTEVENT_PCOUT PORTEVENT_PMIN PORTEVENT_PMOUT PORTEVENT_PQUEUE PORTEVENT_STATE PORTEVENT_UNQUALIFIED STATISTICS_UNQUALIFIED STATISTICS_VERDICT TESTCASE_FINISH TESTCASE_START TESTCASE_UNQUALIFIED TIMEROP_GUARD TIMEROP_READ TIMEROP_START TIMEROP_STOP TIMEROP_TIMEOUT TIMEROP_UNQUALIFIED USER_UNQUALIFIED VERDICTOP_FINAL VERDICTOP_GETVERDICT VERDICTOP_SETVERDICT VERDICTOP_UNQUALIFIED WARNING_UNQUALIFIED"),externalCommands:A("BeginControlPart EndControlPart BeginTestCase EndTestCase"),multiLineStrings:true})});