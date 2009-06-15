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

	initialize: function(target, options){
		this._input = null;
		this.options = {
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

			target: $(target).addClassName('ajax-upload')
		};

		// Merge the users options with our defaults
		Object.extend(this.options, options);

		this._addForm();
		this._addTargetListeners();
	},

	// removes ajaxupload
	destroy: function(){
		if (this._input){
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
				name: AjaxUpload.getUID(),
				method: 'post',
				enctype: 'multipart/form-data',
				encoding: 'multipart/form-data',
				action: this.options.action
		});

		$(document.body).insert(form);

		// Create hidden input element for each data key
		for (var prop in this.options.data){
			form.insert(new Element('input', {type: 'hidden', name: prop, value: this.options.data[prop]}));
		}

		// Add input element for this form:
		this._input = new Element('input', { type: 'file', name: this.options.name, 'class': 'ajax-upload' });
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

		return form;
	},

	_addTargetListeners: function () {
		if (!this._input) {
			throw('AjaxUpload: No input to observe provided!');
		}

		// 1. Submiting file:
		Event.observe(this._input, 'change', function(event) {
			var file = this.getFileName();
			var ext = this.getFileExt();

			if (this.options.onChange && this.options.onChange(this, file, ext, this.options) == false) {
				return;
			}

			// Submit form when value is changed
			if (this.options.autoSubmit) {
				this.submit();
			}
		}.bind(this));

		// 2. When mouse is on the target (invisible input is over it)
		Event.observe(document, 'mousemove', function(event) {
			this._input = $(this._input);
			if (!this._input) {
				return;
			} else if (this._disabled) {
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
			src: "about:blank;",
			name: id,
			id: id});

		$(document.body).insert(iframe.hide());
		Event.observe(iframe, 'load', this.options.iframeOnLoad.bind(this));

		return iframe;
	},

	/**
	 * Upload file without refreshing the page
	 */
	submit: function(){
		if (!this.fileProvided()) {
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
			this._input.value = '';
		}
	},

	getFileName: function() {
		return this._input &&  this._input.value ?
				this._input.value.replace(/.*(\/|\\)/, "") : '';
	},

	getFileExt: function() {
		var file = this._input ? this._input.value : null;
		return file ? (/[.]/.exec(file) ? /[^.]+$/.exec(file.toLowerCase()) : '') : '';
	},

	fileProvided: function() {
		return this._input && this._input.value != '';
	},

	defaultIframeOnLoad: function(event) {
		var iframe = event.target ? event.element() : null;
		_ap.debug('File upload iframe onload - ' + (iframe && iframe.src));
		if (iframe && iframe.src != 'about:blank') {
			var doc = AjaxUpload.getDocument(iframe);
			var content = AjaxUpload.getContent(doc);

			_ap.debug('File upload iframe onload - content: "'
					+ (content ? content.substring(0, 80) : content)
					+ (content && content.length > 80 ? '...' : '')
					+ '" (length: ' + content.length + ')');

			if (content && content.indexOf('OK') == 0 || content.indexOf('FAIL') == 0) {
				AjaxUpload.onLoad(iframe, this.options, this.getFileName(), content);
			} else if (doc.body) {
				doc.body.innerHTML = '';
			}

			iframe = null;
			doc = null;
		}
	}
});

Object.extend(AjaxUpload, {
	getBox: function(element) {
		var offset = $(element) ? $(element).cumulativeOffset() : null;
		return !offset ? null : {
			left: offset.left,
			right: offset.left + element.offsetWidth,
			top: offset.top,
			bottom: offset.top + element.offsetHeight
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
			response = doc.body.innerHTML;
			if (response && response.isJSON()) {
				response = response.evalJSON();
			}

		// 3. response is a xml document:
		} else {
			response = doc;
		}

		return response;
	},

	onLoad: function(iframe, settings, file, content) {
		var failMsg;

		if (content.indexOf('FAIL(') == 0) {
			failMsg = content.substring(content.indexOf('(') + 1, content.lastIndexOf(')'));
		}

		settings.onComplete.call(this, file, content, failMsg, settings);

		// Reload blank page, so that reloading main page
		// does not re-submit the post. Also, remember to
		// delete the frame
		iframe.src = "about:blank"; //load event fired
		iframe.remove();
	}
});
