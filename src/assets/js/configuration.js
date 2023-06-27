function savePreferences()
{
    try{
        JSInterface.saveThePreferences(jQuery("#secondarydisplayurl").val(),jQuery("#advertisementsdisplayurl").val())
    }catch(Err)
    {
        bootbox.alert(Err.stack+"<br />"+Err.message);
    }
}
function goToSecondaryDisplayPage()
{
    try{
        JSInterface.goToSecondaryDisplayPage();
    }catch(Err)
    {
        bootbox.alert(Err.stack+"<br />"+Err.message);
    }
}
jQuery(document).ready(function(){
    window.setTimeout(function(){

        var parameters = JSON.parse(JSInterface.initializeParameters());
        jQuery("#secondarydisplayurl").val(parameters.secondary_display_url);
        jQuery("#advertisementsdisplayurl").val(parameters.advertisements_display_url);

    },300);
});