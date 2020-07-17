/* nice-validator 1.0.7
 * (c) 2012-2016 Jony Zhang <niceue@live.com>, MIT Licensed
 * https://github.com/niceue/nice-validator
 */
(function(A){typeof module==="object"&&module.exports?module.exports=A(require("jquery")):typeof define==="function"&&define.amd?define(["jquery"],A):A(jQuery)}(function(G,g){var Aa="validator",n="."+Aa,a=".rule",z=".field",D=".form",L="nice-"+Aa,H="msg-box",p="aria-required",x="aria-invalid",T="data-rule",A="data-msg",V="data-tip",W="data-ok",j="data-timely",Y="data-target",N="data-display",X="data-must",e="novalidate",m=":verifiable",P=/(&)?(!)?\b(\w+)(?:\[\s*(.*?\]?)\s*\]|\(\s*(.*?\)?)\s*\))?\s*(;|\|)?/g,u=/(\w+)(?:\[\s*(.*?\]?)\s*\]|\(\s*(.*?\)?)\s*\))?/,s=/(?:([^:;\(\[]*):)?(.*)/,t=/[^\x00-\xff]/g,E=/top|right|bottom|left/,b=/(?:(cors|jsonp):)?(?:(post|get):)?(.+)/i,I=/[<>'"`\\]|&#x?\d+[A-F]?;?|%3[A-F]/gmi,d=G.noop,Ab=G.proxy,q=G.trim,B=G.isFunction,U=function(Ac){return typeof Ac==="string"},k=function(Ac){return Ac&&Object.prototype.toString.call(Ac)==="[object Object]"},c=document.documentMode||+(navigator.userAgent.match(/MSIE (\d+)/)&&RegExp.$1),C=function(Ac,Ad,Ae){if(!Ac||!Ac.tagName){return null}if(Ae!==g){if(Ae===null){Ac.removeAttribute(Ad)}else{Ac.setAttribute(Ad,""+Ae)}}else{return Ac.getAttribute(Ad)}},S,y={},O={debug:0,theme:"default",ignore:"",focusInvalid:true,focusCleanup:false,stopOnError:false,beforeSubmit:null,valid:null,invalid:null,validation:null,formClass:"n-default",validClass:"n-valid",invalidClass:"n-invalid",bindClassTo:null},Z={timely:1,display:null,target:null,ignoreBlank:false,showOk:true,dataFilter:function(Ac){if(U(Ac)||(k(Ac)&&("error" in Ac||"ok" in Ac))){return Ac}},msgMaker:function(Ad){var Ac;Ac='<span role="alert" class="msg-wrap n-'+Ad.type+'">'+Ad.arrow;if(Ad.result){G.each(Ad.result,function(Ae,Af){Ac+='<span class="n-'+Af.type+'">'+Ad.icon+'<span class="n-msg">'+Af.msg+"</span></span>"})}else{Ac+=Ad.icon+'<span class="n-msg">'+Ad.msg+"</span>"}Ac+="</span>";return Ac},msgWrapper:"span",msgArrow:"",msgIcon:'<span class="n-icon"></span>',msgClass:"n-right",msgStyle:"",msgShow:null,msgHide:null},w={};G.fn.validator=function(Ad){var Ac=this,Ae=arguments;if(Ac.is(m)){return Ac}if(!Ac.is("form")){Ac=this.find("form")}if(!Ac.length){Ac=this}Ac.each(function(){var Af=G(this).data(Aa);if(Af){if(U(Ad)){if(Ad.charAt(0)==="_"){return}Af[Ad].apply(Af,[].slice.call(Ae,1))}else{if(Ad){Af._reset(true);Af._init(this,Ad)}}}else{new F(this,Ad)}});return this};G.fn.isValid=function(Ac,Af){var Ag=l(this[0]),Ad=B(Ac),Ah,Ae;if(!Ag){return true}if(!Ad&&Af===g){Af=Ac}Ag.checkOnly=!!Af;Ae=Ag.options;Ah=Ag._multiValidate(this.is(m)?this:this.find(m),function(Ai){if(!Ai&&Ae.focusInvalid&&!Ag.checkOnly){Ag.$el.find("["+x+"]:first").focus()}if(Ad){if(Ac.length){Ac(Ai)}else{if(Ai){Ac()}}}Ag.checkOnly=false});return Ad?this:Ah};G.expr.pseudos.verifiable=function(Ac){var Ad=Ac.nodeName.toLowerCase();return(Ad==="input"&&!({submit:1,button:1,reset:1,image:1})[Ac.type]||Ad==="select"||Ad==="textarea"||Ac.contentEditable==="true")&&!Ac.disabled};G.expr.pseudos.filled=function(Ac){return !!q(G(Ac).val())};function F(Ae,Ad){var Ac=this;if(!(Ac instanceof F)){return new F(Ae,Ad)}Ac.$el=G(Ae);if(Ac.$el.length){Af();if(F.pending){G(window).on("validatorready",Af)}}else{if(U(Ae)){y[Ae]=Ad}}function Af(){Ac._init(Ac.$el[0],Ad,!!arguments[0])}}F.prototype={_init:function(Ah,Af,Ae){var Ac=this,Ad,Ag,Ai;if(B(Af)){Af={valid:Af}}Af=Ac._opt=Af||{};Ai=C(Ah,"data-"+Aa+"-option");Ai=Ac._dataOpt=Ai&&Ai.charAt(0)==="{"?(new Function("return "+Ai))():{};Ag=Ac._themeOpt=w[Af.theme||Ai.theme||O.theme];Ad=Ac.options=G.extend({},O,Z,Ag,Ac.options,Af,Ai);if(!Ae){Ac.rules=new Q(Ad.rules,true);Ac.messages=new R(Ad.messages,true);Ac.Field=i(Ac);Ac.elements=Ac.elements||{};Ac.deferred={};Ac.errors={};Ac.fields={};Ac._initFields(Ad.fields)}if(!Ac.$el.data(Aa)){Ac.$el.data(Aa,Ac).addClass(L+" "+Ad.formClass).on("form-submit-validate",function(Ak,Am,Aj,Al,An){Ac.vetoed=An.veto=!Ac.isValid;Ac.ajaxFormOptions=Al}).on("submit"+n+" validate"+n,Ab(Ac,"_submit")).on("reset"+n,Ab(Ac,"_reset")).on("showmsg"+n,Ab(Ac,"_showmsg")).on("hidemsg"+n,Ab(Ac,"_hidemsg")).on("focusin"+n+" click"+n,m,Ab(Ac,"_focusin")).on("focusout"+n+" validate"+n,m,Ab(Ac,"_focusout")).on("keyup"+n+" input"+n+" compositionstart compositionend",m,Ab(Ac,"_focusout")).on("click"+n,":radio,:checkbox","click",Ab(Ac,"_focusout")).on("change"+n,'select,input[type="file"]',"change",Ab(Ac,"_focusout"));Ac._NOVALIDATE=C(Ah,e);C(Ah,e,e)}if(U(Ad.target)){Ac.$el.find(Ad.target).addClass("msg-container")}},_guessAjax:function(Ac){var Ad=this;if(!(Ad.isAjaxSubmit=!!Ad.options.valid)){var Ae=(G._data||G.data)(Ac,"events");Ad.isAjaxSubmit=Af(Ae,"valid","form")||Af(Ae,"submit","form-plugin")}function Af(Ai,Ah,Ag){if(Ai&&Ai[Ah]&&G.map(Ai[Ah],function(Aj){return ~Aj.namespace.indexOf(Ag)?1:null}).length){return true}return false}},_initFields:function(Ai){var Ae=this,Ac,Ag,Ad,Ah=Ai===null;if(Ah){Ai=Ae.fields}if(k(Ai)){for(Ac in Ai){if(~Ac.indexOf(",")){Ag=Ac.split(",");Ad=Ag.length;while(Ad--){Af(q(Ag[Ad]),Ai[Ac])}}else{Af(Ac,Ai[Ac])}}}Ae.$el.find(m).each(function(){Ae._parse(this)});function Af(Al,Ak){if(Ak===null||Ah){var Aj=Ae.elements[Al];if(Aj){Ae._resetElement(Aj,true)}delete Ae.fields[Al]}else{Ae.fields[Al]=new Ae.Field(Al,U(Ak)?{rule:Ak}:Ak,Ae.fields[Al])}}},_parse:function(Ac){var Ag=this,Ai,Ad=Ac.name,Af,Ah,Ae=C(Ac,T);Ae&&C(Ac,T,null);if(Ac.id&&(("#"+Ac.id in Ag.fields)||!Ad||(Ae!==null&&(Ai=Ag.fields[Ad])&&Ae!==Ai.rule&&Ac.id!==Ai.key))){Ad="#"+Ac.id}if(!Ad){return}Ai=Ag.getField(Ad,true);Ai.rule=Ae||Ai.rule;if(Af=C(Ac,N)){Ai.display=Af}if(Ai.rule){if(C(Ac,X)!==null||/\b(?:match|checked)\b/.test(Ai.rule)){Ai.must=true}if(/\brequired\b/.test(Ai.rule)){Ai.required=true;C(Ac,p,true)}if(Ah=C(Ac,j)){Ai.timely=+Ah}else{if(Ai.timely>3){C(Ac,j,Ai.timely)}}Ag._parseRule(Ai);Ai.old={}}if(U(Ai.target)){C(Ac,Y,Ai.target)}if(U(Ai.tip)){C(Ac,V,Ai.tip)}return Ag.fields[Ad]=Ai},_parseRule:function(Ad){var Ac=s.exec(Ad.rule);if(!Ac){return}Ad._i=0;if(Ac[1]){Ad.display=Ac[1]}if(Ac[2]){Ad._rules=[];Ac[2].replace(P,function(){var Ae=arguments;Ae[4]=Ae[4]||Ae[5];Ad._rules.push({and:Ae[1]==="&",not:Ae[2]==="!",or:Ae[6]==="|",method:Ae[3],params:Ae[4]?G.map(Ae[4].split(", "),q):g})})}},_multiValidate:function(Ae,Af){var Ac=this,Ad=Ac.options;Ac.hasError=false;if(Ad.ignore){Ae=Ae.not(Ad.ignore)}Ae.each(function(){Ac._validate(this);if(Ac.hasError&&Ad.stopOnError){return false}});if(Af){Ac.validating=true;G.when.apply(null,G.map(Ac.deferred,function(Ag){return Ag})).done(function(){Af.call(Ac,!Ac.hasError);Ac.validating=false})}return !G.isEmptyObject(Ac.deferred)?g:!Ac.hasError},_submit:function(Ad){var Af=this,Ag=Af.options,Ae=Ad.target,Ac=Ad.type==="submit"&&!Ad.isDefaultPrevented();Ad.preventDefault();if(S&&~(S=false)||Af.submiting||Ad.type==="validate"&&Af.$el[0]!==Ae||B(Ag.beforeSubmit)&&Ag.beforeSubmit.call(Af,Ae)===false){return}if(Af.isAjaxSubmit===g){Af._guessAjax(Ae)}Af._debug("log","\n<<< event: "+Ad.type);Af._reset();Af.submiting=true;Af._multiValidate(Af.$el.find(m),function(Ai){var Aj=(Ai||Ag.debug===2)?"valid":"invalid",Ah;if(!Ai){if(Ag.focusInvalid){Af.$el.find("["+x+"]:first").focus()}Ah=G.map(Af.errors,function(Ak){return Ak})}Af.submiting=false;Af.isValid=Ai;B(Ag[Aj])&&Ag[Aj].call(Af,Ae,Ah);Af.$el.trigger(Aj+D,[Ae,Ah]);Af._debug("log",">>> "+Aj);if(!Ai){return}if(Af.vetoed){G(Ae).ajaxSubmit(Af.ajaxFormOptions)}else{if(Ac&&!Af.isAjaxSubmit){document.createElement("form").submit.call(Ae)}}})},_reset:function(Ac){var Ad=this;Ad.errors={};if(Ac){Ad.reseting=true;Ad.$el.find(m).each(function(){Ad._resetElement(this)});delete Ad.reseting}},_resetElement:function(Ac,Ad){this._setClass(Ac,null);this.hideMsg(Ac);if(Ad){C(Ac,p,null)}},_focusin:function(Ad){var Af=this,Ah=Af.options,Ac=Ad.target,Ag,Ae;if(Af.validating||(Ad.type==="click"&&document.activeElement===Ac)){return}if(Ah.focusCleanup){if(C(Ac,x)==="true"){Af._setClass(Ac,null);Af.hideMsg(Ac)}}Ae=C(Ac,V);if(Ae){Af.showMsg(Ac,{type:"tip",msg:Ae})}else{if(C(Ac,T)){Af._parse(Ac)}if(Ag=C(Ac,j)){if(Ag===8||Ag===9){Af._focusout(Ad)}}}},_focusout:function(An){var Ah=this,As=Ah.options,Ag=An.target,Ad=An.type,Ak,Al=Ad==="focusin",Ai=Ad==="validate",Ae,Ac,Ao,Aq,Aj,Af,Ar,Am,Ap=0;if(Ad==="compositionstart"){Ah.pauseValidate=true}if(Ad==="compositionend"){Ah.pauseValidate=false}if(Ah.pauseValidate){return}Ae=Ag.name&&M(Ag)?Ah.$el.find('input[name="'+Ag.name+'"]').get(0):Ag;if(!(Ac=Ah.getField(Ae))){return}Ak=Ac._e;Ac._e=Ad;Am=Ac.timely;if(!Ai){if(!Am||(M(Ag)&&Ad!=="click")){return}Aq=Ac.getValue();if(Ac.ignoreBlank&&!Aq&&!Al){Ah.hideMsg(Ag);return}if(Ad==="focusout"){if(Ak==="change"){return}if(Am===2||Am===8){if(Aq){Ao=Ac.old;if(Ac.isValid&&!Ao.showOk){Ah.hideMsg(Ag)}else{Ah._makeMsg(Ag,Ac,Ao)}}else{return}}}else{if(Am<2&&!An.data){return}Aj=+new Date();if(Aj-(Ag._ts||0)<100){return}Ag._ts=Aj;if(Ad==="keyup"){if(Ak==="input"){return}Af=An.keyCode;Ar={8:1,9:1,16:1,32:1,46:1};if(Af===9&&!Aq){return}if(Af<48&&!Ar[Af]){return}}if(!Al){Ap=Am<100?(Ad==="click"||Ag.tagName==="SELECT")?0:400:Am}}}if(As.ignore&&G(Ag).is(As.ignore)){return}clearTimeout(Ac._t);if(Ap){Ac._t=setTimeout(function(){Ah._validate(Ag,Ac)},Ap)}else{if(Ai){Ac.old={}}Ah._validate(Ag,Ac)}},_setClass:function(Ac,Ae){var Af=G(Ac),Ad=this.options;if(Ad.bindClassTo){Af=Af.closest(Ad.bindClassTo)}Af.removeClass(Ad.invalidClass+" "+Ad.validClass);if(Ae!==null){Af.addClass(Ae?Ad.validClass:Ad.invalidClass)}},_showmsg:function(Ad,Ac,Ag){var Af=this,Ae=Ad.target;if(Af.$el.is(Ae)){if(k(Ac)){Af.showMsg(Ac)}else{if(Ac==="tip"){Af.$el.find(m+"["+V+"]",Ae).each(function(){Af.showMsg(this,{type:Ac,msg:Ag})})}}}else{Af.showMsg(Ae,{type:Ac,msg:Ag})}},_hidemsg:function(Ac){var Ad=G(Ac.target);if(Ad.is(m)){this.hideMsg(Ad)}},_validatedField:function(Ac,Ah,Ag){var Af=this,Ad=Af.options,Ae=Ah.isValid=Ag.isValid=!!Ag.isValid,Ai=Ae?"valid":"invalid";Ag.key=Ah.key;Ag.ruleName=Ah._r;Ag.id=Ac.id;Ag.value=Ah.value;Af.elements[Ah.key]=Ag.element=Ac;Af.isValid=Af.$el[0].isValid=Ae?Af.isFormValid():Ae;if(Ae){Ag.type="ok"}else{if(Af.submiting){Af.errors[Ah.key]=Ag.msg}Af.hasError=true}Ah.old=Ag;B(Ah[Ai])&&Ah[Ai].call(Af,Ac,Ag);B(Ad.validation)&&Ad.validation.call(Af,Ac,Ag);G(Ac).attr(x,Ae?null:true).trigger(Ai+z,[Ag,Af]);Af.$el.triggerHandler("validation",[Ag,Af]);if(Af.checkOnly){return}Af._setClass(Ac,Ag.skip||Ag.type==="tip"?null:Ae);Af._makeMsg.apply(Af,arguments)},_makeMsg:function(Ac,Ae,Ad){if(Ae.msgMaker){Ad=G.extend({},Ad);if(Ae._e==="focusin"){Ad.type="tip"}this[Ad.showOk||Ad.msg||Ad.type==="tip"?"showMsg":"hideMsg"](Ac,Ad,Ae)}},_validatedRule:function(Af,Ai,Am,Ac){Ai=Ai||Aj.getField(Af);Ac=Ac||{};var Aj=this,Ad,Ah,Ak=Ai._r,An=Ai.timely,Ao=An===9||An===8,Ae,Ag,Al=false;if(Am===null){Aj._validatedField(Af,Ai,{isValid:true,skip:true});Ai._i=0;return}else{if(Am===g){Ae=true}else{if(Am===true||Am===""){Al=true}else{if(U(Am)){Ad=Am}else{if(k(Am)){if(Am.error){Ad=Am.error}else{Ad=Am.ok;Al=true}}}}}}Ah=Ai._rules[Ai._i];if(Ah.not){Ad=g;Al=Ak==="required"||!Al}if(Ah.or){if(Al){while(Ai._i<Ai._rules.length&&Ai._rules[Ai._i].or){Ai._i++}}else{Ae=true}}else{if(Ah.and){if(!Ai.isValid){Ae=true}}}if(Ae){Al=true}else{if(Al){if(Ai.showOk!==false){Ag=C(Af,W);Ad=Ag===null?U(Ai.ok)?Ai.ok:Ad:Ag;if(!U(Ad)&&U(Ai.showOk)){Ad=Ai.showOk}if(U(Ad)){Ac.showOk=Al}}}if(!Al||Ao){Ad=(v(Af,Ai,Ad||Ah.msg||Aj.messages[Ak])||Aj.messages.fallback).replace(/\{0\|?([^\}]*)\}/,function(Aq,Ap){return Aj._getDisplay(Af,Ai.display)||Ap||Aj.messages[0]})}if(!Al){Ai.isValid=Al}Ac.msg=Ad;G(Af).trigger((Al?"valid":"invalid")+a,[Ak,Ad])}if(Ao&&(!Ae||Ah.and)){if(!Al&&!Ai._m){Ai._m=Ad}Ai._v=Ai._v||[];Ai._v.push({type:Al?!Ae?"ok":"tip":"error",msg:Ad||Ah.msg})}Aj._debug("log","   "+Ai._i+": "+Ak+" => "+(Al||Ad));if((Al||Ao)&&Ai._i<Ai._rules.length-1){Ai._i++;Aj._checkRule(Af,Ai)}else{Ai._i=0;if(Ao){Ac.isValid=Ai.isValid;Ac.result=Ai._v;Ac.msg=Ai._m||"";if(!Ai.value&&(Ai._e==="focusin")){Ac.type="tip"}}else{Ac.isValid=Al}Aj._validatedField(Af,Ai,Ac);delete Ai._m;delete Ai._v}},_checkRule:function(Ad,Af){var Ai=this,Ak,Ag,Ac,Aj=Af.key,Ae=Af._rules[Af._i],Ah=Ae.method,Al=Ae.params;if(Ai.submiting&&Ai.deferred[Aj]){return}Ac=Af.old;Af._r=Ah;if(Ac&&!Af.must&&!Ae.must&&Ae.result!==g&&Ac.ruleName===Ah&&Ac.id===Ad.id&&Af.value&&Ac.value===Af.value){Ak=Ae.result}else{Ag=h(Ad,Ah)||Ai.rules[Ah]||d;Ak=Ag.call(Af,Ad,Al,Af);if(Ag.msg){Ae.msg=Ag.msg}}if(k(Ak)&&B(Ak.then)){Ai.deferred[Aj]=Ak;Af.isValid=g;!Ai.checkOnly&&Ai.showMsg(Ad,{type:"loading",msg:Ai.messages.loading},Af);Ak.then(function(Am,Ar,Aq){var An=q(Aq.responseText),Ap,Ao=Af.dataFilter;if(/jsonp?/.test(this.dataType)){An=Am}else{if(An.charAt(0)==="{"){An=G.parseJSON(An)}}Ap=Ao.call(this,An,Af);if(Ap===g){Ap=Ao.call(this,An.data,Af)}Ae.data=this.data;Ae.result=Af.old?Ap:g;Ai._validatedRule(Ad,Af,Ap)},function(Am,An){Ai._validatedRule(Ad,Af,Ai.messages[An]||An)}).always(function(){delete Ai.deferred[Aj]})}else{Ai._validatedRule(Ad,Af,Ak)}},_validate:function(Ac,Ae){var Ad=this;if(Ac.disabled||C(Ac,e)!==null){return}Ae=Ae||Ad.getField(Ac);if(!Ae){return}if(!Ae._rules){Ad._parse(Ac)}if(!Ae._rules){return}Ad._debug("info",Ae.key);Ae.isValid=true;Ae.element=Ac;Ae.value=Ae.getValue();if(!Ae.required&&!Ae.must&&!Ae.value){if(!M(Ac)){Ad._validatedField(Ac,Ae,{isValid:true});return true}}Ad._checkRule(Ac,Ae);return Ae.isValid},_debug:function(Ac,Ad){if(window.console&&this.options.debug){console[Ac](Ad)}},test:function(Ac,Ad){var Ag=this,Ai,Af=u.exec(Ad),Ae,Ah,Aj;if(Af){Ah=Af[1];if(Ah in Ag.rules){Aj=Af[2]||Af[3];Aj=Aj?Aj.split(", "):g;Ae=Ag.getField(Ac,true);Ae._r=Ah;Ae.value=Ae.getValue();Ai=Ag.rules[Ah].call(Ae,Ac,Aj)}}return Ai===true||Ai===g||Ai===null},_getDisplay:function(Ac,Ad){return !U(Ad)?B(Ad)?Ad.call(this,Ac):"":Ad},_getMsgOpt:function(Ac,Ad){var Ae=Ad?Ad:this.options;return G.extend({type:"error",pos:o(Ae.msgClass),target:Ae.target,wrapper:Ae.msgWrapper,style:Ae.msgStyle,cls:Ae.msgClass,arrow:Ae.msgArrow,icon:Ae.msgIcon},U(Ac)?{msg:Ac}:Ac)},_getMsgDOM:function(Af,Ad){var Aj=G(Af),Ae,Ac,Ai,Ah;if(Aj.is(m)){Ai=Ad.target||C(Af,Y);if(Ai){Ai=B(Ai)?Ai.call(this,Af):this.$el.find(Ai);if(Ai.length){if(Ai.is(m)){Af=Ai.get(0)}else{if(Ai.hasClass(H)){Ae=Ai}else{Ah=Ai}}}}if(!Ae){Ac=(!M(Af)||!Af.name)&&Af.id?Af.id:Af.name;Ae=this.$el.find(Ad.wrapper+"."+H+'[for="'+Ac+'"]')}}else{Ae=Aj}if(!Ad.hide&&!Ae.length){Aj=this.$el.find(Ai||Af);Ae=G("<"+Ad.wrapper+">").attr({"class":H+(Ad.cls?" "+Ad.cls:""),"style":Ad.style||g,"for":Ac});if(M(Af)){var Ag=Aj.parent();Ae.appendTo(Ag.is("label")?Ag.parent():Ag)}else{if(Ah){Ae.appendTo(Ah)}else{Ae[!Ad.pos||Ad.pos==="right"?"insertAfter":"insertBefore"](Aj)}}}return Ae},showMsg:function(Ah,Ae,Aj){if(!Ah){return}var Ac=this,Ad=Ac.options,Ak,Af,Ai,Ag;if(k(Ah)&&!Ah.jquery&&!Ae){G.each(Ah,function(Am,An){var Al=Ac.elements[Am]||Ac.$el.find(J(Am))[0];Ac.showMsg(Al,An)});return}if(G(Ah).is(m)){Aj=Aj||Ac.getField(Ah)}if(!(Af=(Aj||Ad).msgMaker)){return}Ae=Ac._getMsgOpt(Ae,Aj);Ah=G(Ah).get(0);if(!Ae.msg&&Ae.type!=="error"){Ai=C(Ah,"data-"+Ae.type);if(Ai!==null){Ae.msg=Ai}}if(!U(Ae.msg)){return}Ag=Ac._getMsgDOM(Ah,Ae);!E.test(Ag[0].className)&&Ag.addClass(Ae.cls);if(c===6&&Ae.pos==="bottom"){Ag[0].style.marginTop=G(Ah).outerHeight()+"px"}Ag.html(Af.call(Ac,Ae))[0].style.display="";if(B(Ak=Aj&&Aj.msgShow||Ad.msgShow)){Ak.call(Ac,Ag,Ae.type)}},hideMsg:function(Ac,Af,Ag){var Ad=this,Ae=Ad.options,Ah,Ai;Ac=G(Ac).get(0);if(G(Ac).is(m)){Ag=Ag||Ad.getField(Ac);if(Ag){if(Ag.isValid||Ad.reseting){C(Ac,x,null)}}}Af=Ad._getMsgOpt(Af,Ag);Af.hide=true;Ai=Ad._getMsgDOM(Ac,Af);if(!Ai.length){return}if(B(Ah=Ag&&Ag.msgHide||Ae.msgHide)){Ah.call(Ad,Ai,Af.type)}else{Ai[0].style.display="none";Ai[0].innerHTML=null}},getField:function(Ac,Af){var Ae=this,Ad,Ag;if(U(Ac)){Ad=Ac;Ac=g}else{if(C(Ac,T)){return Ae._parse(Ac)}if(Ac.id&&"#"+Ac.id in Ae.fields||!Ac.name){Ad="#"+Ac.id}else{Ad=Ac.name}}if((Ag=Ae.fields[Ad])||Af&&(Ag=new Ae.Field(Ad))){Ag.element=Ac}return Ag},setField:function(Ac,Ad){var Ae={};if(!Ac){return}if(U(Ac)){Ae[Ac]=Ad}else{Ae=Ac}this._initFields(Ae)},isFormValid:function(){var Ae=this.fields,Ac,Ad;for(Ac in Ae){Ad=Ae[Ac];if(!Ad._rules||!Ad.required&&!Ad.must&&!Ad.value){continue}if(!Ad.isValid){return false}}return true},holdSubmit:function(Ac){this.submiting=Ac===g||Ac},cleanUp:function(){this._reset(1)},destroy:function(){this._reset(1);this.$el.off(n).removeData(Aa);C(this.$el[0],e,this._NOVALIDATE)}};function i(Ac){function Ae(){var Ag=this.options;for(var Af in Ag){if(Af in Z){this[Af]=Ag[Af]}}G.extend(this,{_valHook:function(){if(this.element){return this.element.contentEditable==="true"?"text":"val"}},getValue:function(){var Ah=this.element;if(Ah){if(Ah.type==="number"&&Ah.validity&&Ah.validity.badInput){return"NaN"}return G(Ah)[this._valHook()]()}else{return null}},setValue:function(Ah){G(this.element)[this._valHook()](this.value=Ah)},getRangeMsg:function(An,Ao,Au){if(!Ao){return}var Ar=this,Am=Ar.messages[Ar._r]||"",Al,Aj=Ao[0].split("~"),Ah=Ao[1]==="false",At=Aj[0],Ak=Aj[1],As="rg",Ap=[""],Aq=q(An)&&+An===+An;function Ai(Aw,Av){return !Ah?Aw>=Av:Aw>Av}if(Aj.length===2){if(At&&Ak){if(Aq&&Ai(An,+At)&&Ai(+Ak,An)){Al=true}Ap=Ap.concat(Aj);As=Ah?"gtlt":"rg"}else{if(At&&!Ak){if(Aq&&Ai(An,+At)){Al=true}Ap.push(At);As=Ah?"gt":"gte"}else{if(!At&&Ak){if(Aq&&Ai(+Ak,An)){Al=true}Ap.push(Ak);As=Ah?"lt":"lte"}}}}else{if(An===+At){Al=true}Ap.push(At);As="eq"}if(Am){if(Au&&Am[As+Au]){As+=Au}Ap[0]=Am[As]}return Al||Ar._rules&&(Ar._rules[Ar._i].msg=Ar.renderMsg.apply(null,Ap))},renderMsg:function(){var Aj=arguments,Ai=Aj[0],Ah=Aj.length;if(!Ai){return}while(--Ah){Ai=Ai.replace("{"+Ah+"}",Aj[Ah])}return Ai}})}function Ad(Ag,Ah,Af){this.key=Ag;this.validator=Ac;G.extend(this,Af,Ah)}Ae.prototype=Ac;Ad.prototype=new Ae();return Ad}function Q(Ad,Ac){if(!k(Ad)){return}var Af,Ae=Ac?Ac===true?this:Ac:Q.prototype;for(Af in Ad){if(f(Af)){Ae[Af]=r(Ad[Af])}}}function R(Ad,Ac){if(!k(Ad)){return}var Af,Ae=Ac?Ac===true?this:Ac:R.prototype;for(Af in Ad){Ae[Af]=Ad[Af]}}function r(Ac){switch(G.type(Ac)){case"function":return Ac;case"array":var Ad=function(){return Ac[0].test(this.value)||Ac[1]||false};Ad.msg=Ac[1];return Ad;case"regexp":return function(){return Ac.test(this.value)}}}function l(Ac){var Ad,Af,Ae;if(!Ac||!Ac.tagName){return}switch(Ac.tagName){case"INPUT":case"SELECT":case"TEXTAREA":case"BUTTON":case"FIELDSET":Ad=Ac.form||G(Ac).closest("."+L);break;case"FORM":Ad=Ac;break;default:Ad=G(Ac).closest("."+L)}for(Af in y){if(G(Ad).is(Af)){Ae=y[Af];break}}return G(Ad).data(Aa)||G(Ad)[Aa](Ae).data(Aa)}function h(Ac,Ae){var Ad=q(C(Ac,T+"-"+Ae));if(Ad&&(Ad=new Function("return "+Ad)())){return r(Ad)}}function v(Ac,Af,Ae){var Ag=Af.msg,Ad=Af._r;if(k(Ag)){Ag=Ag[Ad]}if(!U(Ag)){Ag=C(Ac,A+"-"+Ad)||C(Ac,A)||(Ae?U(Ae)?Ae:Ae[Ad]:"")}return Ag}function o(Ad){var Ac;if(Ad){Ac=E.exec(Ad)}return Ac&&Ac[0]}function M(Ac){return Ac.tagName==="INPUT"&&Ac.type==="checkbox"||Ac.type==="radio"}function K(Ac){return Date.parse(Ac.replace(/\.|\-/g,"/"))}function f(Ac){return/^\w+$/.test(Ac)}function J(Ac){var Ad=Ac.charAt(0)==="#";Ac=Ac.replace(/([:.{(|)}/\[\]])/g,"\\$1");return Ad?Ac:'[name="'+Ac+'"]:first'}G(window).on("beforeunload",function(){this.focus()});G(document).on("click",":submit",function(){var Ad=this,Ac;if(!Ad.form){return}Ac=Ad.getAttributeNode("formnovalidate");if(Ac&&Ac.nodeValue!==null||C(Ad,e)!==null){S=true}}).on("focusin submit validate","form,."+L,function(Ad){if(C(this,e)!==null){return}var Ac=G(this),Ae;if(!Ac.data(Aa)&&(Ae=l(this))){if(!G.isEmptyObject(Ae.fields)){if(Ad.type==="focusin"){Ae._focusin(Ad)}else{Ae._submit(Ad)}}else{C(this,e,e);Ac.off(n).removeData(Aa)}}});new R({fallback:"This field is not valid.",loading:"Validating..."});new Q({required:function(Ad,Af){var Ah=this,Ae=q(Ah.value),Ai=true;if(Af){if(Af.length===1){if(!f(Af[0])){if(!Ae&&!G(Af[0],Ah.$el).length){return null}}else{if(Ah.rules[Af[0]]){if(!Ae&&!Ah.test(Ad,Af[0])){C(Ad,p,null);return null}else{C(Ad,p,true)}}}}else{if(Af[0]==="not"){G.each(Af.slice(1),function(){return(Ai=Ae!==q(this))})}else{if(Af[0]==="from"){var Ag=Ah.$el.find(Af[1]),Ac="_validated_",Aj;Ai=Ag.filter(function(){var Ak=Ah.getField(this);return Ak&&!!q(Ak.getValue())}).length>=(Af[2]||1);if(Ai){if(!Ae){Aj=null}}else{Aj=v(Ag[0],Ah)||false}if(!G(Ad).data(Ac)){Ag.data(Ac,1).each(function(){if(Ad!==this){Ah._validate(this)}}).removeData(Ac)}return Aj}}}}return Ai&&!!Ae},integer:function(Ag,Ah){var Ad,Af="0|",Ae="[1-9]\\d*",Ac=Ah?Ah[0]:"*";switch(Ac){case"+":Ad=Ae;break;case"-":Ad="-"+Ae;break;case"+0":Ad=Af+Ae;break;case"-0":Ad=Af+"-"+Ae;break;default:Ad=Af+"-?"+Ae}Ad="^(?:"+Ad+")$";return new RegExp(Ad).test(this.value)||this.messages.integer[Ac]},match:function(Ad,Ag){if(!Ag){return}var Ac=this,Al,Ai,Aj,Ae,Ak="eq",An,Af,Ah,Am;if(Ag.length===1){Aj=Ag[0]}else{Ak=Ag[0];Aj=Ag[1]}Af=J(Aj);Ah=Ac.$el.find(Af)[0];if(!Ah){return}Am=Ac.getField(Ah);Al=Ac.value;Ai=Am.getValue();if(!Ac._match){Ac.$el.on("valid"+z+n,Af,function(){G(Ad).trigger("validate")});Ac._match=Am._match=1}if(!Ac.required&&Al===""&&Ai===""){return null}An=Ag[2];if(An){if(/^date(time)?$/i.test(An)){Al=K(Al);Ai=K(Ai)}else{if(An==="time"){Al=+Al.replace(/:/g,"");Ai=+Ai.replace(/:/g,"")}}}if(Ak!=="eq"&&!isNaN(+Al)&&isNaN(+Ai)){return true}Ae=Ac.messages.match[Ak].replace("{1}",Ac._getDisplay(Ad,Am.display||Aj));switch(Ak){case"lt":return(+Al<+Ai)||Ae;case"lte":return(+Al<=+Ai)||Ae;case"gte":return(+Al>=+Ai)||Ae;case"gt":return(+Al>+Ai)||Ae;case"neq":return(Al!==Ai)||Ae;default:return(Al===Ai)||Ae}},range:function(Ac,Ad){return this.getRangeMsg(this.value,Ad)},checked:function(Af,Ag){if(!M(Af)){return}var Ae=this,Ad,Ac;if(Af.name){Ac=Ae.$el.find('input[name="'+Af.name+'"]').filter(function(){var Ah=this;if(!Ad&&M(Ah)){Ad=Ah}return !Ah.disabled&&Ah.checked}).length}else{Ad=Af;Ac=Ad.checked}if(Ag){return Ae.getRangeMsg(Ac,Ag)}else{return !!Ac||v(Ad,Ae,"")||Ae.messages.required}},length:function(Ae,Af){var Ad=this.value,Ac=(Af[1]==="true"?Ad.replace(t,"xx"):Ad).length;return this.getRangeMsg(Ac,Af,(Af[1]?"_2":""))},remote:function(Ae,Ai){if(!Ai){return}var Ac=this,Ak=b.exec(Ai[0]),Aj=Ac._rules[Ac._i],Ag={},Ad="",Al=Ak[3],Am=Ak[2]||"POST",Ah=(Ak[1]||"").toLowerCase(),Af;Aj.must=true;Ag[Ae.name]=Ac.value;if(Ai[1]){G.map(Ai.slice(1),function(Ao){var Ap,An;if(~Ao.indexOf("=")){Ad+="&"+Ao}else{Ap=Ao.split(":");Ao=q(Ap[0]);An=q(Ap[1])||Ao;Ag[Ao]=Ac.$el.find(J(An)).val()}})}Ag=G.param(Ag)+Ad;if(!Ac.must&&Aj.data&&Aj.data===Ag){return Aj.result}if(Ah!=="cors"&&/^https?:/.test(Al)&&!~Al.indexOf(location.host)){Af="jsonp"}return G.ajax({url:encodeURI(Al),type:Am,data:Ag,dataType:Af})},filter:function(Ad,Ae){var Ac=this.value,Af=Ac.replace(Ae?(new RegExp("["+Ae[0]+"]","gm")):I,"");if(Af!==Ac){this.setValue(Af)}}});F.config=function(Ad,Ae){if(k(Ad)){G.each(Ad,Ac)}else{if(U(Ad)){Ac(Ad,Ae)}}function Ac(Ag,Af){if(Ag==="rules"){new Q(Af)}else{if(Ag==="messages"){new R(Af)}else{if(Ag in Z){Z[Ag]=Af}else{O[Ag]=Af}}}}};F.setTheme=function(Ad,Ac){if(k(Ad)){G.extend(true,w,Ad)}else{if(U(Ad)&&k(Ac)){w[Ad]=G.extend(w[Ad],Ac)}}};F.load=function(Af){if(!Af){return}var Ai=document,Ah={},Ae=Ai.scripts[0],Ac,Ad,Ag;Af.replace(/([^?=&]+)=([^&#]*)/g,function(Ak,Aj,Al){Ah[Aj]=Al});Ac=Ah.dir||F.dir;if(!F.css&&Ah.css!==""){Ad=Ai.createElement("link");Ad.rel="stylesheet";Ad.href=F.css=Ac+"jquery.validator.css";Ae.parentNode.insertBefore(Ad,Ae)}if(!F.local&&~Af.indexOf("local")&&Ah.local!==""){F.local=(Ah.local||Ai.documentElement.lang||"en").replace("_","-");F.pending=1;Ad=Ai.createElement("script");Ad.src=Ac+"local/"+F.local+".js";Ag="onload" in Ad?"onload":"onreadystatechange";Ad[Ag]=function(){if(!Ad.readyState||/loaded|complete/.test(Ad.readyState)){Ad=Ad[Ag]=null;delete F.pending;G(window).triggerHandler("validatorready")}};Ae.parentNode.insertBefore(Ad,Ae)}};(function(){var Ad=document.scripts,Ac=Ad.length,Ae,Af,Ag=/(.*validator(?:\.min)?.js)(\?.*(?:local|css|dir)(?:=[\w\-]*)?)?/;while(Ac--&&!Af){Ae=Ad[Ac];Af=(Ae.hasAttribute?Ae.src:Ae.getAttribute("src",4)||"").match(Ag)}if(!Af){return}F.dir=Af[1].split("/").slice(0,-1).join("/")+"/";F.load(Af[2])})();return G[Aa]=F}));