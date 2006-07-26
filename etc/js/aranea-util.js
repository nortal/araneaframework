function getElementByIdORName(str) {
  var r = document.getElementById(str);
  if (r)
    return r;
  r = document.getElementsByName(str).item(0);
  return r;
}

function setElementAttr(elementStr, attrName, attrValue) {
  var el = getElementByIdORName(elementStr);
  if (el) {
    el.setAttribute(attrName, attrValue);
  }
}
