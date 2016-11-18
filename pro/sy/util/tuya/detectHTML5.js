function detectHTML5() {
    try {
        document.createElement('canvas').getContext('2d');
    } catch (e) {
      alert("您的浏览器不支持html5 canvas，请下载一个更加先进的浏览器。");
//        window.location = '/html5notsupport';
    }
}
