using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.ComponentModel.DataAnnotations;
using System.Web.Security;
using Telerik.Web.Mvc;
using SmartVitals_Server.Models;
using SmartVitals_Server.Context;

namespace SmartVitals_Server.Controllers
{
    public class AdministrationController : Controller
    {
        //
        // GET: /Administrator/
        [Authorize(Roles="Administrator")]
        public ActionResult Index()
        {
            return View();
        }

        [Authorize(Roles = "Administrator")]
        public ActionResult Users()
        {
            var users = Membership.GetAllUsers();
            foreach (MembershipUser user in users)
            {
                //user.
            }
            return View(Membership.GetAllUsers());
        }

        [Authorize(Roles = "Administrator")]
        [GridAction]
        public ActionResult GetUsers()
        {
            var users = Membership.GetAllUsers();
            var usersLists = new List<User>();
            foreach(MembershipUser user in users)
            {
                usersLists.Add(new User
                {
                    UserName = user.UserName,
                    Email = user.Email,
                    Roles = String.Join(", ", Roles.GetRolesForUser(user.UserName).ToList()),
                    LastActivity = user.LastLoginDate
                });
            }
            return View(new GridModel<User>(usersLists));
        }
    }
}
