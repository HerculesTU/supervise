<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="图片轮播新闻" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
</#if>
  <plattag:picslide height="${HEIGHT}" onclickfn="${CLICKFN_NAME}" dyna_param="${DYNA_PARAM}" 
  dyna_interface="${DYNA_INTERFACE}" id="${CONTROL_ID}"></plattag:picslide>
<script type="text/javascript">
  ${CLICKFN_CONTENT}
  $(function(){
	jQuery("#${CONTROL_ID}").slide({ mainCell:".bd ul",effect:"top",autoPlay:true,triggerTime:0 });
  });
</script>
<#if PLAT_DESIGNMODE?? >
</div>
</#if>
