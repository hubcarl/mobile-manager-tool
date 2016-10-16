$(document).ready(function(){
	Validator.initForm($('#input-form'), true);
    var dklv_type_option = [{'value' : 1, 'text' : '年利率'}, {'value' : 2, 'text' : '月利率'}];
    obj_dklv_type = new Combox('dklv_type', 'dklv_type', dklv_type_option, 1, 168, 29, {listDivId:"dklv_type_list", onchange:function(){zhlv();}});

    var dkqx_option = [{'value': 60, 'text': '5年'},
                      {'value': 120, 'text': '10年'},
                      {"value": 180, 'text': '15年'},
                      {'value': 240, 'text': '20年'},
                      {'value': 300, 'text': '25年'},
                      {'value': 360, 'text': '30年'}];
    obj_dkqx_sel_1 = new Combox('dkqx_sel_1', 'dkqx_1', dkqx_option, 240, 168, 29, {listDivId:"dkqx_list_1", onchange:function(){cal_lv(1);}});
    obj_dkqx_sel_2 = new Combox('dkqx_sel_2', 'dkqx_2', dkqx_option, 240, 168, 29, {listDivId:"dkqx_list_2", onchange:function(){cal_lv(2);}});
    obj_dkqx_sel_3 = new Combox('dkqx_sel_3', 'dkqx_3', dkqx_option, 240, 168, 29, {listDivId:"dkqx_list_3", onchange:function(){cal_lv(3);}});
    
    var dklv_option = [{'value': 0.7, 'text': '最新基准利率7折'},
                      {'value': 0.8, 'text': '最新基准利率8折'},
    				  {'value': 0.83, 'text': '最新基准利率8.3折'},
                      {'value': 0.85, 'text': '最新基准利率8.5折'},
                      {'value': 0.88, 'text': '最新基准利率8.8折'},
                      {'value': 0.9, 'text': '最新基准利率9折'},
                      {'value': 0.95, 'text': '最新基准利率9.5折'},
                      {'value': 1, 'text': '最新基准利率'},
                      {'value': 1.05, 'text': '最新基准利率1.05倍'},
                      {'value': 1.1, 'text': '最新基准利率1.1倍'},
                      {'value': 1.2, 'text': '最新基准利率1.2倍'},
                      {'value': 1.3, 'text': '最新基准利率1.3倍'}];
    var gjjlv_option = [{'value': 3.75, 'text': '5年以下'},
                      {'value': 4.00, 'text': '公积金基准利率'},
                      {'value': 4.40, 'text': '公积金基准利率1.1倍'}];
    

    obj_dklv_sel_1 = new Combox('dklv_sel_1', "lvbs_1", dklv_option, 1, 168, 29, {listDivId:"dklv_list_1", onchange:function(){cal_lv(1);}});
    obj_dklv_sel_2 = new Combox('dklv_sel_2', "lvbs_2", gjjlv_option, 4.00, 168, 29, {listDivId:"dklv_list_2", onchange:function(){cal_lv(2);}});
    obj_dklv_sel_3 = new Combox('dklv_sel_3', "lvbs_3", dklv_option, 1, 168, 29, {listDivId:"dklv_list_3", onchange:function(){cal_lv(3);}});

    var sfbl_option = [{'value': 2, 'text': '2成'},
                      {'value': 3, 'text': '3成'},
                      {'value': 4, 'text': '4成'},
                      {'value': 5, 'text': '5成'},
                      {'value': 6, 'text': '6成'},
                      {'value': 7, 'text': '7成'},
                      {'value': 8, 'text': '8成'},
                      {'value': 9, 'text': '9成'}];

    obj_sfbl_sel_2 = new Combox('sfbl_sel_2', "sfbl_2", sfbl_option, 3, 168, 29, {listDivId:"sfbl_list_2"});

    //按揭房贷
    if ($('input[name=sy_type]').length > 0)
    {
        obj_sfbl_sel_1 = new Combox('sfbl_sel_1', "sfbl_1", sfbl_option, 3, 168, 29, {listDivId:"sfbl_list_1"});
    }

    //计算利率
    cal_lv(1);

    $('.cal-btn').each(function(){
        $(this).click(function(){
        	if(Validator.validForm($('#input-form'), true)){
        		var did = $(this).attr('did');
	            if(gocal(did)){
                    var $body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html') : $('body')) : $('html,body');
                    if($.browser.webkit)
                        $body = $('body');
	                $('.save-result-btn').show();
                    //var top = $('.save-result-btn').offset().top;
                    //$body.animate({ scrollTop:top}, 350);
	            }
        	}
        	return false;
        });
    });
    $('.reset-btn').each(function(){
        $(this).click(function(){
            var f = $(this).attr('did');
            if (f == 1)
            {
                if ($('input[name=sy_type]').length > 0)
                {
                    if ($('input[name=sy_type]:checked').val() == 1)
                    {
                        $('#dkje_1').val('');
                        obj_dkqx_sel_1.setValue(240);
                        cal_lv(1);
                    }
                    else
                    {
                        $('#pmdj_1').val('');
                        $('#mj_1').val('');
                        $($('input[name=sy_gfxz]')[0]).attr('checked', true);
                        obj_dkqx_sel_1.setValue(240);
                        cal_sf();
                        cal_lv(1);
                    }
                }
                else
                {
                    $('#dkje_1').val('');
                    obj_dkqx_sel_1.setValue(240);
                    obj_dklv_sel_1.setValue(1);
                    obj_dklv_type.setValue(1);
                    cal_lv(1);
                }
            }
            else if (f == 2)
            {
                if ($('input[name=gjj_type]:checked').val() == 1)
                {
                    $('#dkje_2').val('');
                    obj_dkqx_sel_2.setValue(240);
                    cal_lv(2);
                }
                else
                {
                    $('#pmdj').val('');
                    $('#mj').val('');
                    $($('input[name=gfxz]')[0]).attr('checked', true);
                    obj_dkqx_sel_2.setValue(240);
                    cal_sf();
                    cal_lv(2);
                }
            }
            else if (f == 3)
            {
                $('#dkje_3_1').val('');
                $('#dkje_3_2').val('');
                obj_dkqx_sel_3.setValue(240);
                obj_dklv_sel_3.setValue(1);
                cal_lv(3);
            }
            clear_result();
            remove_err();
            return false;
        });
    });

    $('.recalculate').each(function(){
        $(this).click(function(){
            var $body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html') : $('body')) : $('html,body');
            if($.browser.webkit)
                $body = $('body');
            var top = $('#content-main').offset().top;
            $body.animate({ scrollTop:top}, 350);
            
        });      
    });

    $('input[name=gjj_type]').change(function(){
        var t = $(this).val();
        $('.div2_' + 2/parseInt(t)).each(function(){
            $(this).hide();
        });
        $('.div2_' + t).each(function(){
            $(this).show();
        });
        remove_err();
        clear_result();
    });

    //按揭房贷
    if ($('input[name=sy_type]').length > 0)
    {
        $('input[name=sy_type]').change(function(){
            var t = $(this).val();
            $('.div1_' + 2/parseInt(t)).each(function(){
                $(this).hide();
            });
            $('.div1_' + t).each(function(){
                $(this).show();
            });
            remove_err();
            clear_result();
        });

        $('input[name=sy_gfxz]').change(function(){
            cal_sf();
            cal_lv(1);
        });
    }

    $('input[name=gfxz]').change(function(){
        cal_sf();
        cal_lv(2);
    });

    $('#mj').change(function(){
        cal_sf();
        cal_lv(2);
    });

    $('.help').each(function(){
        $(this).click(function(evt){
            evt.stopPropagation();
            $('#' + $(this).attr('id') + '_help').toggle();
        });
    });

    $('.close').each(function(){
        $(this).click(function(){
            $(this).parent().hide();
        });
    });
    
    $('input[name=dkqx_1]').change(function(){
    	cal_lv(1);
    });

    $(document).click(function(){
        $('.tsk').each(function(){
            $(this).hide();    
        });
        $('.yt-list').each(function(){
            $(this).hide();    
        });
    });
    cal_lv($('#calc-lid').val());
    remove_err();
    clear_result();
    
    var je = getRequestParam('je');
    var lv = getRequestParam('lv');
    var qx = getRequestParam('qx');
    if(lv){
        obj_dklv_sel_1.setValue(lv);
    }
    if(qx){
        $('input[name=dkqx_1]').val(qx);
    }
    if(je){
        $('#dkje_1').val(je);
        gocal(1);
    }
});

function remove_err()
{
    $('.err').each(function(){
        $(this).remove();
    });
    $('.inpt').each(function(){
        $(this).css('border', '1px solid #D6D6D6');
    });
}

function clear_result()
{
    //重置结果
    $('#_debx_dkje').html(0);
    $('#_debx_dkqx').html(0);
    $('#_debx_myhk').html(0);
    $('#_debx_zflx').html(0);
    $('#_debx_hkze').html(0);
    $('#_debj_dkje').html(0);
    $('#_debj_dkqx').html(0);
    $('#_debj_syhk').html(0);
    $('#_debj_mydj').html(0);
    $('#_debj_zflx').html(0);
    $('#_debj_hkze').html(0);
}

//年、月利率转换
function zhlv()
{
    //原来是月利率，修改后为年利率
    var lv_val = parseFloat($('#dklv_1').val());
    if (!lv_val)
    {
        return false;
    }
    if ($('input[name=dklv_type]').val() == 1)
    {
        var new_lv = lv_val * 12;
        $('#dklv_1').val(parseFloat(new_lv).toFixed(2) + '%');
    }
    else
    {
        var new_lv = lv_val / 12;
        $('#dklv_1').val(parseFloat(new_lv).toFixed(4) + '%');
    }
}

//计算首付比例
function cal_sf()
{
    if ($('input[name=sy_type]').length > 0)
    {
        if ($('input[name=sy_gfxz]:checked').val() == 1)
        {
            if (parseFloat($('#mj_1').val()) <= 90)
            {
                obj_sfbl_sel_1.setValue(2);
            }
            else
            {
                obj_sfbl_sel_1.setValue(3);
            }
        }
        else
        {
            obj_sfbl_sel_1.setValue(6);
        }
    }
    else
    {
        if ($('input[name=gfxz]:checked').val() == 1)
        {
            if (parseFloat($('#mj').val()) <= 90)
            {
                obj_sfbl_sel_2.setValue(2);
            }
            else
            {
                obj_sfbl_sel_2.setValue(3);
            }
        }
        else
        {
            obj_sfbl_sel_2.setValue(6);
        }
    }
}
function cal_sy_lv(qx, bs)
{
    var jzlv = [5.35, 5.75, 5.75, 5.75, 5.90];
    if (qx <= 6)
    {
        lv = jzlv[0];
    }
    else if (qx > 6 && qx <= 12)
    {
        lv = jzlv[1];
    }
    else if (qx > 12 && qx <= 36)
    {
        lv = jzlv[2];
    }
    else if (qx > 36 && qx <= 60)
    {
        lv = jzlv[3];
    }
    else
    {
        lv = jzlv[4];
    }
    var zzlv = (lv * bs).toFixed(2);
    return zzlv;
}

function cal_gjj_lv(qx)
{
    var gjjlv = [3.50, 4.00];
    var zzlv = 0;
    if (qx <= 60)
    {
        zzlv = gjjlv[0];
    }
    else
    {
        zzlv = gjjlv[1];
    }
    
    //按面积计算，二套房，利率1.1倍
    if ($('input[name=gjj_type]:checked').val() == 2 && $('input[name=gfxz]:checked').val() == 2)
    {
        zzlv = parseFloat(zzlv) * 1.1;
    }
    return zzlv;
}

function cal_lv(f)
{
    var qx = parseInt($('input[name=dkqx_' + f + ']').val()) * 12;
    var lv = 0;
    var zjlv = 0;
    if (f == 1)
    {
        zjlv = cal_sy_lv(qx, parseFloat($('input[name=lvbs_' + f + ']').val()));
        if ($('input[name=dklv_type]').val() == 2)
        {
            zjlv = zjlv / 12;
            $('#dklv_' + f).val(parseFloat(zjlv).toFixed(4) + '%');
        }
        else
        {
            $('#dklv_' + f).val(parseFloat(zjlv).toFixed(2) + '%');
        }
    }
    else if (f == 2)
    {
        //zjlv = cal_gjj_lv(qx);
        zjlv = parseFloat($('input[name=lvbs_2]').val());
        $('#dklv_' + f).val(parseFloat(zjlv).toFixed(2) + '%');
    }
    else if (f == 3)
    {
        var zjlv1 = cal_gjj_lv(qx);
        var zjlv2 = cal_sy_lv(qx, parseFloat($('input[name=lvbs_' + f + ']').val()));
        $('#dklv_' + f + '_1').val(parseFloat(zjlv1).toFixed(2) + '%');
        $('#dklv_' + f + '_2').val(parseFloat(zjlv2).toFixed(2) + '%');
    }
}

//组合贷计算
function cal_zh()
{
    var qx = $('input[name=dkqx_3]').val();
    var gjj_je = parseFloat($('#dkje_3_1').val());
    var gjj_lv = parseFloat($('#dklv_3_1').val());
    var sy_je = parseFloat($('#dkje_3_2').val());
    var sy_lv = parseFloat($('#dklv_3_2').val());

    if (!gjj_je || isNaN(gjj_je) || gjj_je > 10000000)
    {
        show_err('dkje_3_1', '请输入正确的公积金贷款金额', 13);
        $('#dkje_3_1').css('border', '1px solid #FF6633').focus();
        return false;
    }
    else
    {
        $('#dkje_3_1').css('border', '1px solid #D6D6D6');
        $('#dkje_3_1_err').remove();
    }
    if (!gjj_lv || isNaN(gjj_lv) || gjj_lv > 100)
    {
        show_err('dklv_3_1', '请输入正确的公积金贷款利率');
        $('#dklv_3_1').css('border', '1px solid #FF6633').focus();
        return false;
    }
    else
    {
        $('#dklv_3_1').css('border', '1px solid #D6D6D6');
        $('#dklv_3_1_err').remove();
    }
    if (!sy_je || isNaN(sy_je) || sy_je > 10000000)
    {
        show_err('dkje_3_2', '请输入正确的商业贷款金额', 13);
        $('#dkje_3_2').css('border', '1px solid #FF6633').focus();
        return false;
    }
    else
    {
        $('#dkje_3_2').css('border', '1px solid #D6D6D6');
        $('#dkje_3_2_err').remove();
    }
    if (!sy_lv || isNaN(sy_lv) || sy_lv > 100)
    {
        show_err('dklv_3_2', '请输入正确的商业贷款利率');
        $('#dklv_3_2').css('border', '1px solid #FF6633').focus();
        return false;
    }
    else
    {
        $('#dklv_3_2').css('border', '1px solid #D6D6D6');
        $('#dklv_3_2_err').remove();
    }

    var lvlx = 1;
    var xx = 1;
    var gjj_debx_jg = debx(gjj_je * 10000, gjj_lv, qx, lvlx, xx);
    var sy_debx_jg = debx(sy_je * 10000, sy_lv, qx, lvlx, xx);
    $('#_debx_dkje').html(fmoney((gjj_je + sy_je) * 10000, 2));
    $('#_debx_dkqx').html(qx);
    $('#_debx_myhk').html(fmoney(gjj_debx_jg.yhk + sy_debx_jg.yhk, 2));
    $('#_debx_zflx').html(fmoney(gjj_debx_jg.zlx + sy_debx_jg.zlx, 2));
    $('#_debx_hkze').html(fmoney(gjj_debx_jg.hkze + sy_debx_jg.hkze, 2));

    var gjj_debj_jg = debj(gjj_je * 10000, gjj_lv, qx, lvlx, xx);
    var sy_debj_jg = debj(sy_je * 10000, sy_lv, qx, lvlx, xx);
    $('#_debj_dkje').html(fmoney((gjj_je + sy_je) * 10000, 2));
    $('#_debj_dkqx').html(qx);
    $('#_debj_syhk').html(fmoney(gjj_debj_jg.syhk + sy_debj_jg.syhk, 2));
    $('#_debj_mydj').html(fmoney(gjj_debj_jg.mydj + sy_debj_jg.mydj, 2));
    $('#_debj_zflx').html(fmoney(gjj_debj_jg.zlx + sy_debj_jg.zlx, 2));
    $('#_debj_hkze').html(fmoney(gjj_debj_jg.hkze + sy_debj_jg.hkze, 2));
    
    window.CalculatorResult = {
        type: 3,
        je: fmoney((gjj_je + sy_je) * 10000, 2),
        gjj_je: gjj_je,
        sy_je: sy_je,
        qx: qx,
        gjj_lv: gjj_lv,
        sy_lv: sy_lv,
        debx_myhk: fmoney(gjj_debx_jg.yhk + sy_debx_jg.yhk, 2),
        debx_zflx: fmoney(gjj_debx_jg.zlx + sy_debx_jg.zlx, 2),
        debx_hkze: fmoney(gjj_debx_jg.hkze + sy_debx_jg.hkze, 2),
        debj_syhk: fmoney(gjj_debj_jg.syhk + sy_debj_jg.syhk, 2),
        debj_mydj: fmoney(gjj_debj_jg.mydj + sy_debj_jg.mydj, 2),
        debj_zflx: fmoney(gjj_debj_jg.zlx + sy_debj_jg.zlx, 2),
        debj_hkze: fmoney(gjj_debj_jg.hkze + sy_debj_jg.hkze, 2)
    };
    return true;
}

function gocal(f)
{
    if (f == 3)
    {
        return cal_zh();
    }
    var je_input;
    //公积金按面积算时，先计算贷款总额
    if (f == 2 && $('input[name=gjj_type]:checked').val() == 2)
    {
        var mpmdj = parseFloat($('#pmdj').val());
        var fwmj = parseFloat($('#mj').val());
        if (!mpmdj || isNaN(mpmdj) || mpmdj > 200000)
        {
            show_err('pmdj', '请输入房屋平米单价', 40);
            $('#pmdj').css('border', '1px solid #FF6633').focus();
            return false;
        }
        else
        {
            $('#pmdj').css('border', '1px solid #D6D6D6');
            $('#pmdj_err').remove();
        }
        if (!fwmj || isNaN(fwmj) || fwmj > 500)
        {
            show_err('mj', '请输入正确的房屋面积', 25);
            $('#mj').css('border', '1px solid #FF6633').focus();
            return false;
        }
        else
        {
            $('#mj').css('border', '1px solid #D6D6D6');
            $('#mj_err').remove();
        }
        var fwzj = mpmdj * fwmj; //房屋总价
        var je = fwzj * (1 - parseInt($('input[name=sfbl_2]').val()) * 0.1);
        je_input = je.toFixed(2) + '元';
    }
    //商业贷款按面积算时，先计算贷款总额
    else if (f == 1 && $('input[name=sy_type]').length > 0 && $('input[name=sy_type]:checked').val() == 2)
    {
        var mpmdj = parseFloat($('#pmdj_1').val());
        var fwmj = parseFloat($('#mj_1').val());
        if (!mpmdj || isNaN(mpmdj) || mpmdj > 200000)
        {
            show_err('pmdj_1', '请输入房屋平米单价', 40);
            $('#pmdj_1').css('border', '1px solid #FF6633').focus();
            return false;
        }
        else
        {
            $('#pmdj_1').css('border', '1px solid #D6D6D6');
            $('#pmdj_1_err').remove();
        }
        if (!fwmj || isNaN(fwmj) || fwmj > 500)
        {
            show_err('mj_1', '请输入正确的房屋面积', 25);
            $('#mj_1').css('border', '1px solid #FF6633').focus();
            return false;
        }
        else
        {
            $('#mj_1').css('border', '1px solid #D6D6D6');
            $('#mj_1_err').remove();
        }
        var fwzj = mpmdj * fwmj; //房屋总价
        var je = fwzj * (1 - parseInt($('input[name=sfbl_1]').val()) * 0.1);
        je_input = je.toFixed(2) + '元';
    }
    else
    {
        var je = parseFloat($('#dkje_' + f).val()) * 10000;
        je_input = $('#dkje_' + f).val() + '万元';
    }
    var lv = parseFloat($('#dklv_' + f).val());
    var qx = $('input[name=dkqx_' + f + ']').val() * 12;
    if (!je || isNaN(je) || je > 100000000)
    {
        show_err('dkje_' + f, '请输入正确的贷款金额', 13);
        $('#dkje_' + f).css('border', '1px solid #FF6633').focus();
        return false;
    }
    else
    {
        $('#dkje_' + f).css('border', '1px solid #D6D6D6');
        $('#dkje_' + f + '_err').remove();
    }
    if (!lv || isNaN(lv) || lv > 100)
    {
        show_err('dklv_' + f, '请输入正确的贷款利率');
        $('#dklv_' + f).css('border', '1px solid #FF6633').focus();
        return false;
    }
    else
    {
        $('#dklv_' + f).css('border', '1px solid #D6D6D6');
        $('#dklv_' + f + '_err').remove();
    }

    var lvlx = 1;
    if (f == 1 && $('input[name=dklv_type]').val() == 2)
    {
        lvlx = 2;
    }

    var xx = 0;
    var debx_jg = debx(je, lv, qx, lvlx, xx);
    $('#_debx_dkje').html(fmoney(je, 2));
    $('#_debx_dkqx').html(qx);
    $('#_debx_myhk').html(fmoney(debx_jg.yhk, 2));
    $('#_debx_zflx').html(fmoney(debx_jg.zlx, 2));
    $('#_debx_hkze').html(fmoney(debx_jg.hkze, 2));

    var debj_jg = debj(je, lv, qx, lvlx, xx);
    $('#_debj_dkje').html(fmoney(je, 2));
    $('#_debj_dkqx').html(qx);
    $('#_debj_syhk').html(fmoney(debj_jg.syhk, 2));
    $('#_debj_mydj').html(fmoney(debj_jg.mydj, 2));
    $('#_debj_zflx').html(fmoney(debj_jg.zlx, 2));
    $('#_debj_hkze').html(fmoney(debj_jg.hkze, 2));
    
    window.CalculatorResult = {
        type: f,
        je_input: je_input,
        je: fmoney(je, 2),
        qx: qx,
        lv: lv,
        debx_myhk: fmoney(debx_jg.yhk, 2),
        debx_zflx: fmoney(debx_jg.zlx, 2),
        debx_hkze: fmoney(debx_jg.hkze, 2),
        debj_syhk: fmoney(debj_jg.syhk, 2),
        debj_mydj: fmoney(debj_jg.mydj, 2),
        debj_zflx: fmoney(debj_jg.hkze, 2),
        debj_hkze: fmoney(debj_jg.hkze, 2)
    };

    return true;
}

function fmoney(s, n)  
{  
    n = n > 0 && n <= 20 ? n : 2;  
    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";  
    var l = s.split(".")[0].split("").reverse(),  
    r = s.split(".")[1];  
    t = "";  
    for(i = 0; i < l.length; i ++ )  
    {  
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");  
    }  
    return t.split("").reverse().join("") + "." + r;  
}

/**
 * 取得url中的参数
 * @param {Object} name
 */
function getRequestParam(name){
    var url = location.href; 
    var paraString = url.substring(url.indexOf("?")+1,url.length).split("&"); 
    var paraObj = {};
    for (i=0; j=paraString[i]; i++){ 
        paraObj[j.substring(0,j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=")+1,j.length); 
    } 
    var returnValue = paraObj[name.toLowerCase()]; 
    if(typeof(returnValue)=="undefined"){ 
        return ""; 
    }else{ 
        return returnValue; 
    } 
}
