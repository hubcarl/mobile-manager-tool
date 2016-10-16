/**
 * 下拉框,替代select
 * 
 * @param {[string]} containerId  外层divID, 用于存放combox
 * @param * {[string]} textDivId    
 * @param * {[type]} listDiv      
 * @param {[type]} inputName    生成的input标签的name属性
 * @param {[type]} option       选项数组,每个元素为{text:'xx',value:'xxx' }
 * @param {[type]} defaultValue 默认值
 * @param {[type]} width        宽度
 * @param {[type]} height       高度
 * @param {[object]} options    可选参数
 *   options.onchange       change回调函数, 传入当前值
 *   options.oninit         初始化回调函数, 传入默认值
 *   options.textDivId      显示当前选项的div id
 *   options.listDivId      列表div id
 */
function Combox(containerId, inputName, data, defaultValue, width, height, options){
  var me = this;
  me.divId_ = containerId;
  me.options = options || {};
  me.option_ = data;
  me.textDiv_ = me.options.textDivId || 't' + (+new Date);
  me.listDiv_ = me.options.listDivId || 'l' + (+new Date);
  me.inputName_ = inputName;
  me.currentValue_ = defaultValue;
  me.width_ = width;
  me.height_ = height;
  me.div_ = $('#' + containerId);
  me.disible_ = null;
  me.nodes_ = [];
  //外层元素不存在
  if(! me.div_[0]){
    return;
  }
  me.initialize();
}

Combox.prototype.initialize = function(){
  var lDiv,
    me = this,
    tDiv = me.text_ = $('<div></div>')
      .addClass('yt-text')
      .attr('id',me.textDiv_)
      .height(me.height_ - 2)
      .css('lineHeight', (me.height_ - 2) + 'px' )
      .width(me.width_)
      .click(function(evt){
        evt.stopPropagation();
        lDiv.toggle()
          .children().removeClass('list-div-hover');
        lDiv.find('[ivalue="'+me.currentValue_+'"]').addClass('list-div-hover');
      });
  //设置显示文本
  $.each(me.option_, function(idx, val){
    if(val.value == me.currentValue_){
      tDiv.html(val.text);
    }
  });
  tDiv.appendTo(this.div_);
  /*
  var img = me.img_ = $('<img />')
    .attr('src', '/static/img/v2/searchbox_select.png')
    .addClass('sb_select_img');
  img.appendTo(tDiv);
   */

  lDiv = $('<div></div>')
    .attr('panes', 'layer')
    .attr('id', me.listDiv_)
    .addClass('yt-list')
    .css('top', me.height_ - 1)
    .css('left', 0)
    .width(me.width_ + 8)
    .hide();
  lDiv.appendTo(me.div_);

  $.each(me.option_, function(idx, val){
    var nodeDiv = $('<div></div>')
      .attr('ivalue', val.value)
      .width(me.width_ - 12)
      .height(25)
      .css('lineHeight', 25 + 'px')
      .css('cursor','pointer')
      .html(val.text)
      .hover(function(){
        $(this).toggleClass('item-on-hover');
      })
      .click(function(evt){
        //evt.stopPropagation();
        lDiv.hide();
        tDiv.html($(this).html());
        //tDiv.append(me.img_);
        var oldValue = me.currentValue_;
        me.inp_.val(me.currentValue_ = $(this).attr('ivalue'));
        /*
        if(me.setHighLight){
          me.setHighLight(me.currentValue_);
        }
        */
        if (me.options.onchange && (oldValue != me.currentValue_)) {
          me.options.onchange(me.currentValue_, oldValue);
        };
      })
      ;
    if(me.currentValue_ == val.value){
      nodeDiv.addClass('list-div-hover');
    }
	if(me.options.dataTrack){
		nodeDiv.attr('data-track',me.options.dataTrack+'|'+val.value);
	}
    nodeDiv.appendTo(lDiv);
    me.nodes_.push(nodeDiv);
  });
  var inp = me.inp_ = $('<input type="hidden" name="' + this.inputName_ +'"/>')
    .attr('id','_combox_inp_'+me.divId_)
    .val(me.currentValue_);
  inp.appendTo(me.div_);
  if(me.options.oninit){
    me.options.oninit(me.currentValue_);
  }
};

Combox.prototype.setValue = function(value){
  var me = this;
  $.each(me.nodes_, function(idx, val){
    if(val.attr('ivalue') == value){
      me.text_.html(val.html());
      //me.text_.append(me.img_);
      //me.inp_.val(value);

      var oldValue = me.currentValue_;
      me.inp_.val(me.currentValue_ = value);
      if (me.options.onchange && (oldValue != me.currentValue_)) {
        me.options.onchange(me.currentValue_, oldValue);
      };
    }
  });
};

