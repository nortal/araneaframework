/**
 * Ajax upload
 * Project page - http://valums.com/ajax-upload/
 * Copyright (c) 2008 Andris Valums, http://valums.com
 * Licensed under the MIT license (http://valums.com/mit-license/)
 * Version 3.0 (05.04.2009)
 */

/**
 * Changes from the previous version
 * 1. Fixed Opera 9.64 response problem
 *
 * For the full changelog please visit:
 * http://valums.com/ajax-upload-changelog/
 */

// assigning methods to our class
var AjaxUpload = Class.create({

	initialize: function(target, options) {
		this._input = null;
		this.options = {

			// A flag to disable submitting files
			disabled: false,

			// Location of the server-side upload script
			action: '',

			// File upload name
			name: '',

			// Additional parameters to send
			data: {},

			// Submit file as soon as it's selected
			autoSubmit: true,

			// When user selects a file, useful with autoSubmit disabled
			onChange: function(file, extension, options) {},

			// Callback to fire before file is uploaded
			// You can return false to cancel upload
			onSubmit: function(file, extension, options) {},

			// Fired when file upload is completed
			onComplete: function(file, responseText, failMsg, options) {},

			// Fired when hidden iframe is loaded. Usually you don't need to override it.
			iframeOnLoad: this.defaultIframeOnLoad,

			target: $(target)
		};

		// Merge the users options with our defaults
		Object.extend(this.options, options);

		this._addForm();

		this._addInputListener()
		this._addDocumentListener()
	},

	// removes ajaxupload
	destroy: function() {
		if (this._input) {
			this._input.up('form').remove();
			this._input = null;
		}
		this.options = null;
	},

	/**
	 * Creates form, that will be submitted to iframe
	 */
	_addForm: function() {
		var form = new Element('form', {
				method: 'post',
				enctype: 'multipart/form-data',
				encoding: 'multipart/form-data'
		});

		form.action = this.options.action;

		$(document.body).insert(form);

		// Create hidden input element for each data key
		for (var prop in this.options.data){
			form.insert(new Element('input', {type: 'hidden', name: prop, value: this.options.data[prop]}));
		}

		this._addInput(form);

		return form;
	},

	_addInput: function(form) {
		if (!form) throw('AjaxUplaod._addInput: "form" is required!');

		// Add input element for this form:
		this._input = new Element('input', { type: 'file', name: this.options.name, 'class': 'ajax-upload-input' });
		this._input.setStyle({
			position : 'absolute',
			margin: '-5px 0 0 -175px',
			padding: 0,
			width: '220px',
			height: '10px',
			opacity: 0,
			cursor: 'hand',
			zIndex :  2147483583 //Max zIndex supported by Opera 9.0-9.2x 
		});

		form.insert(this._input.hide());
	},

	// Resets file input (this._input) value.
	_resetInput: function() {
		this._input.value = '';

		if (this._input.value) {
			var form = this._input.up('form');
			this._input.remove();
			this._addInput(form);
			this._addInputListener()
			form = null;
		}
	},

	_addInputListener: function () {
		if (!this._input) {
			throw('AjaxUpload: No input to observe provided!');
		}

		// Submiting file:
		Event.observe(this._input, 'change', function(event) {
			var file = this.getFileName();
			var ext = this.getFileExt();

			if (this.options.onChange && this.options.onChange(this, file, ext, this.options) == false) {
				this._resetInput();
				return;
			}

			// Submit form when value is changed
			if (this.options.autoSubmit) {
				this.submit();
			}
		}.bind(this));
	},

	_addDocumentListener: function () {
		if (!this._input) {
			throw('AjaxUpload: No input to observe provided!');
		}

		// When mouse is on the target (invisible input will be placed over it)
		Event.observe(document, 'mousemove', function(event) {
			this._input = $(this._input);
			if (!this._input) {
				return;
			} else if (this.options.disabled) {
				this._input.hide();
				return;
			}

			var pointer = event.pointer();
			var box = AjaxUpload.getBox(this.options.target);

			var test = (pointer.x >= box.left) && (pointer.x <= box.right) &&
				(pointer.y >= box.top) && (pointer.y <= box.bottom);

			if (test) {
				this._input.setStyle({ top: pointer.y + 'px', left: pointer.x + 'px' }).show();
			} else {
				this._input.hide();
			}
		}.bind(this));
	},

	/**
	 * Creates iframe with unique name
	 */
	_addIframe: function() {
		// Unique name
		var id = AjaxUpload.getUID();

		// Remove ie6 "This page contains both secure and nonsecure items" prompt
		// http://tinyurl.com/77w9wh
		var iframe = new Element('iframe', {
			src: "javascript:false;",
			name: id,
			id: id
		});

		$(document.body).insert(iframe.hide());
		Event.observe(iframe, 'load', this.options.iframeOnLoad.bind(this));

		return iframe;
	},

	/**
	 * Upload file without refreshing the page
	 */
	submit: function(){
		if (!this.isFileProvided()) {
			return false;	// there is no file
		}

		var file = this.getFileName();
		var ext = this.getFileExt();

		// execute user event
		if (this.options.onSubmit.call(this, file, ext, this.options) != false) {
			var form = this._input.up('form')
			form.target = this._addIframe().name;
			form.submit();
			form = null;
			// The iframe will remove itself when submit is completed.
		} else { // clear input to allow user to select same file:
			this._resetInput();
		}

		this.options.target.value = ''; // Just in case.
	},

	getFileName: function() {
		return this._input &&  this._input.value ? this._input.value.replace(/.*(\/|\\)/, '') : '';
	},

	getFileExt: function() {
		var file = this._input ? this._input.value : null;
		return (-1 !== file.indexOf('.')) ? file.replace(/.*[.]/, '') : '';
	},

	isFileProvided: function() {
		return this._input && this._input.value != '';
	},

	defaultIframeOnLoad: function(event) {
		var iframe = event.target ? event.element() : null;
		if (iframe && iframe.src != 'about:blank') {
			var doc = AjaxUpload.getDocument(iframe);
			var content = AjaxUpload.getContent(doc);

			if (doc.body) {
				doc.body.innerHTML = '';
			}

			if (content && content.indexOf('OK') == 0 || content.indexOf('FAIL') >= 0) {
				var fileName = this.getFileName();
				this._resetInput();
				AjaxUpload.onLoad(iframe, this.options, fileName, content);
			}

			iframe = null;
			doc = null;
		}
	}
});

Object.extend(AjaxUpload, {

	getOffset: function(el){
		// getOffset function copied from jQuery lib (http://jquery.com/)
		if (document.documentElement["getBoundingClientRect"]){
			// Get Offset using getBoundingClientRect
			// http://ejohn.org/blog/getboundingclientrect-is-awesome/
			var box = el.getBoundingClientRect(),
			doc = el.ownerDocument,
			body = doc.body,
			docElem = doc.documentElement,

			// for ie 
			clientTop = docElem.clientTop || body.clientTop || 0,
			clientLeft = docElem.clientLeft || body.clientLeft || 0,

			// In Internet Explorer 7 getBoundingClientRect property is treated as physical,
			// while others are logical. Make all logical, like in IE8.

			zoom = 1;
			if (body.getBoundingClientRect) {
				var bound = body.getBoundingClientRect();
				zoom = (bound.right - bound.left)/body.clientWidth;
			}
			if (zoom > 1){
				clientTop = 0;
				clientLeft = 0;
			}
			var top = box.top + (window.pageYOffset || docElem && docElem.scrollTop || body.scrollTop) - clientTop,
			left = box.left + (window.pageXOffset|| docElem && docElem.scrollLeft || body.scrollLeft) - clientLeft;

			return {
				top: top,
				left: left
			};
		} else {
			// Get offset adding all offsets 
			if (w.jQuery){
				return jQuery(el).offset();
			}

			var top = 0, left = 0;
			do {
				top += el.offsetTop || 0;
				left += el.offsetLeft || 0;
			}
			while (el = el.offsetParent);

			return {
				left: left,
				top: top
			};
		}
	},

	getBox: function (el) {
		var left, right, top, bottom;
		var offset = this.getOffset(el);
		left = offset.left;
		top = offset.top;
		right = left + el.offsetWidth;
		bottom = top + el.offsetHeight;

		return {
			left: left,
			right: right,
			top: top,
			bottom: bottom
		};
	},

	/**
	 * Function generates unique id
	 */
	getUID: function() {
		this._id = this._id ? this._id : 0;
		return 'AjaxUpload' + this._id++;
	},

	getDocument: function(iframe) {
		return iframe && iframe.contentDocument ? iframe.contentDocument :
			(iframe && frames[iframe.id] ? frames[iframe.id].document : null);
	},

	getContent: function(doc) {
		var response;

		// 1. response is a xml document (IE property):
		if (doc.XMLDocument) {
			response = doc.XMLDocument;

		// 2. response is html document or plain text:
		} else if (doc.body) {
			response = doc.body.innerHTML.stripTags();
			if (response && response.isJSON()) {
				response = response.evalJSON();
			}

		// 3. response is a xml document:
		} else {
			response = doc;
		}

		return response;
	},

	onLoad: function(iframe, settings, fileName, content) {
		// Reload blank page, so that reloading main page does not re-submit the post.
		iframe.src = "about:blank"; //load event fired
		iframe.remove();

		var failMsg;

		if (content.indexOf('FAIL(') == 0) {
			failMsg = content.substring(content.indexOf('(') + 1, content.lastIndexOf(')'));
		}

		settings.onComplete.call(this, fileName, content, failMsg, settings);
	}
});
