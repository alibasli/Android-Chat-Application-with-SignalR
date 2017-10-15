using Social.BL.Services.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace Social.Controllers
{
    public class HomeController : Controller
    {
        private readonly IUserService _lessonService;

         /// <summary>
        /// 
        /// </summary>
        /// <param name="skiCenterService"></param>
        /// <param name="albumService"></param>
        public HomeController(IUserService lessonService)
        {
            _lessonService = lessonService;
           

        }
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult About()
        {
            ViewBag.Message = "Your application description page.";

            return View();
        }

        public ActionResult Contact()
        {
            ViewBag.Message = "Your contact page.";

            return View();
        }
    }
}