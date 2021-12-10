using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.ComponentModel.DataAnnotations;
using SmartVitals_Server.Models;
using System.Web.Security;
using SmartVitals.Services.Models;
using Telerik.Web.Mvc;
using SmartVitals_Server.Context;

namespace SmartVitals_Server.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            if (User.Identity.IsAuthenticated)
            {
                return RedirectToAction("Portal");
            }
            ViewBag.Message = "Welcome to SmartVitals!";

            return View();
        }

        public ActionResult About()
        {
            return View();
        }

        [Authorize(Roles = "Administrator,User")]
        public ActionResult Portal()
        {
            return View(DbHelpers.GetTestsByUserName(User.Identity.Name));
        }

        [Authorize(Roles = "Administrator")]
        public ActionResult Sessions()
        {
                return View(DbHelpers.GetSessions());
        }

        [Authorize(Roles = "Administrator")]
        public JsonResult ClearSessions(int SessionId)
        {
            DbHelpers.RemoveSession(SessionId);
                return Json(true,JsonRequestBehavior.AllowGet);
        }

        [Authorize(Roles="Administrator,User")]
        public ActionResult UserProfile(UserInfo userInfo)   
        {
            if (!String.IsNullOrEmpty(userInfo.UserName))
            {
                UserInfo.Save(userInfo);
                ViewBag.Message = "User Updated";
                return View(UserInfo.GetFromProfile(WebProfile.Current)); 
            }
            MembershipUser user = Membership.GetUser();
            var profile = WebProfile.Current;
            profile.Email = user.Email;

            return View(UserInfo.GetFromProfile(profile));
            
        }

        [Authorize(Roles = "Administrator,User")]
        public ActionResult TestDetails(String TestId)
        {
            var model = DbHelpers.GetTestById(Int32.Parse(TestId), true);
            return View(model);
        }

        [Authorize(Roles = "Administrator")]
        public ActionResult UserAdministration()
        {
            return View();
        }

        [Authorize(Roles = "Administrator")]
        [GridAction]
        public ActionResult GetSessions()
        {
            return View(new GridModel<Session>(DbHelpers.GetSessions()));
        }

        [Authorize(Roles = "Administrator, User")]
        [GridAction]
        public ActionResult GetSamples(int TestId)
        {
            var model = DbHelpers.GetTestById(TestId, true).Samples;
            return View(new GridModel<Sample>(model));
        }
    }
}
