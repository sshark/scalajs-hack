if (process.env.NODE_ENV === "production") {
  const opt = require("./scala-js-hack-opt.js");
  opt.entrypoint.main();
  module.exports = opt;
} else {
  var exports = window;
  exports.require = require("./scala-js-hack-fastopt-entrypoint.js").require;
  window.global = window;

  const fastOpt = require("./scala-js-hack-fastopt.js");
  // fastOpt.entrypoint.main()
  module.exports = fastOpt;

  if (module.hot) {
    module.hot.accept();
  }
}
