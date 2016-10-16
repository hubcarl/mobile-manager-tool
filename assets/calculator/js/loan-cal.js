//等额本息还款法
//je  贷款金额
//lv  利率%，如年利率5.6%就为5.6
//qx  贷款期限（月）
//lvlx 利率类型，1:年利率, 2:月利率
//xx  详细列表
//公式:[贷款本金*月利率*(1+月利率)^还款月数]/[(1+月利率)^还款月数-1] 
function debx(je, lv, qx, lvlx, xx)
{
    qx = parseInt(qx);
    je = parseFloat(je);
    lv = parseFloat(lv);
    lvlx = parseInt(lvlx);
    xx = parseInt(xx);
   
    //月利率
    if (lvlx == 2)
    {
        ylv = lv * 0.01;
    }
    else
    {
        ylv = lv / 12 * 0.01;
    }
   
    var t = Math.pow(1+ylv, qx);
   
    //每月还款
    var yhk=je * ylv * (t/(t -1));
    //累计还款总额
    var hkze=yhk * qx;
    //累计支付利息
    var zlx=hkze - je;
   
    var fh = new Object();
    fh.zlx = zlx;
    fh.hkze = hkze;
    fh.yhk = yhk;

    if (xx == 1)
    {
        var ye = je;    //贷款余额
        var sz = [];
        for (i=1; i<=qx; i++)
        {
            var ylx = ye * ylv;
            var ybj = yhk-ylx;
            ye -= ybj;
            var xj = new Object();
            xj.bh = i;
            xj.ylx = ylx;   //月利息
            xj.ybj = ybj;   //月本金
            xj.ye = ye;     //余额
            sz[i-1] = xj;
        }
        fh.xx = sz;
    }

    return fh;
}

//等额本金还款法
//je贷款金额
//lv年利率%，如年利率5.6%就为5.6
//qx还款年限
function debj(je, lv, qx, lvlx, xx){
    qx=parseInt(qx);
    je=parseFloat(je);
    lv=parseFloat(lv);
    lvlx = parseInt(lvlx);
    xx = parseInt(xx);
   
    //月利率
    if (lvlx == 2)
    {
        ylv = lv * 0.01;
    }
    else
    {
        ylv = lv / 12 * 0.01;
    }
  
    //累计还款总额
    var hkze=0;
    var ybj = je / qx;

    var fh = new Object();
    fh.ybj = ybj;

    var ye = je;
    var sz = [];
    for(i = 1; i <= qx; i++)
    {
        yhk = je/qx + (je-je*(i-1)/qx)*ylv;//第i月还款额
        if (i == 1)
        {
            fh.syhk = yhk;
        }
        if (i == 2)
        {
            fh.mydj = fh.syhk - yhk;
        }
        hkze = hkze + yhk;
        ylx = yhk - ybj;
        ye = ye - ybj;
    
        var xj = new Object();
        xj.bh = i;
        xj.ylx = ylx;   //月利息
        xj.yhk = yhk;   //月还款
        xj.ye = ye;     //余额
        sz[i-1] = xj;
    }

    if (xx == 1)
    {
        fh.xx = sz;
    }

    fh.zlx = hkze - je;
    fh.hkze = hkze;

    return fh;
}

/**
 * xz 房屋性质, 1:普通住宅, 2:非普通住宅
 * m5n 是否满5年, 1:是, 0:否 
 * mj 面积
 * zj 总价(元)
 */
function esfsf(xz, m5n, mj, zj)
{
    xz = parseInt(xz);
    m5n = parseInt(m5n);
    mj = parseFloat(mj);
    zj = parseFloat(zj);
    
    //契税税率
    var qssl = 0;
    if (xz == 2)
    {
        qssl = 3.0;
    }else
    {
        if (mj > 90)
        {
            qssl = 1.5;
        }
        else
        {
            qssl = 1.0;
        }
    }
    var qs = zj * qssl / 100;   //契税

    //营业税税率
    var yyssl = 0.0;
    if (m5n == 0)
    {
        yyssl = 5.55;
    }
    var yys = zj * yyssl / 100; //营业税

    //城建税
    var cjs = yys * 0.07;

    //教育附加费
    var jyfjf = yys * 0.03;

    //个人所得税税率
    var grsds = zj * 0.01;

    //印花税
    var yhs = 0;

    var fh = new Object();
    fh.qs = qs;
    fh.yys = yys;
    fh.cjs = cjs;
    fh.jyfjf = jyfjf;
    fh.grsds = grsds;
    fh.yhs = yhs;
    fh.total = qs + yys + cjs + jyfjf + grsds + yhs;

    return fh;
}

//提示错误
function show_err(id, msg, lft)
{
    if ($('#' + id + '_err').length == 0)
    {
        var t = $('#' + id).offset().top; 
        var l = $('#' + id).offset().left;
        var h = $('#' + id).height();
        var w = $('#' + id).width();
        var zl = 0;
        if (lft && parseInt(lft) > 0)
        {
            zl = lft;
        }
        else
        {
            zl = 0;
        }
        var div = '<div id="' + id + '_err" class="err" style="left:' + (l+w+20+zl) + 'px;top:' + t + 'px;"><div class="con">' + msg + '</div><div class="arr"></div><div style="clear:both"></div></div>';
        $(div).appendTo(document.body);
        //$('#wrapper').after(div);
    }
}
