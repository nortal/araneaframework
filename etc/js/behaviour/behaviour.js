/*
   Note: for Aranea 1.1.1 the selector methods in this file were replaced with
   Prototype's built-in $$() function, very few of original behaviour is left.

   Behaviour v1.1 by Ben Nolan, June 2005. Based largely on the work
   of Simon Willison (see comments by Simon below).

   Description:
   	
   	Uses css selectors to apply javascript behaviours to enable
   	unobtrusive javascript in html documents.
   	
   Usage:   
   
	var myrules = {
		'b.someclass' : function(element){
			element.onclick = function(){
				alert(this.innerHTML);
			}
		},
		'#someid u' : function(element){
			element.onmouseover = function(){
				this.innerHTML = "BLAH!";
			}
		}
	};
	
	Behaviour.register(myrules);
	
	// Call Behaviour.apply() to re-apply the rules (if you
	// update the dom, etc).

   License:
   
   This file is entirely BSD licensed.
   	
   More information:

   	http://ripcord.co.nz/behaviour/
*/   

var Behaviour = {
	list : new Array(),

	register : function(sheet){
		Behaviour.list.push(sheet);
	},

	start : function() {
		Behaviour.addLoadEvent(function() {
			Behaviour.apply();
		});
	},
	
	apply : function() {
		var sheet = null;
		for (var h=0;sheet=Behaviour.list[h];h++){
			for (selector in sheet){
				list = $$(selector);

				if (!list){
					continue;
				}

				for (i=0;element=list[i];i++){
					sheet[selector](element);
				}
			}
		}
	},

	addLoadEvent : function(func) {
		var oldonload = window.onload;

		if (typeof window.onload != 'function') {
			window.onload = func;
		} else {
			window.onload = function() {
				oldonload();
				func();
			}
		}
	}
};

window['behaviour/behaviour.js'] = true;
