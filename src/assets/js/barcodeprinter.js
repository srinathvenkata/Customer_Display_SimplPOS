jQuery(document).ready(function(){
    JSInterface.printLog("Welcome");
    var parameters = JSON.parse(JSInterface.initializeParameters());
    jQuery("#heading1").val(parameters.heading_1);
    jQuery("#heading2").val(parameters.heading_2)
});
function printBarcodes()
{
    JSInterface.printBarcode();
}