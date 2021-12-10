using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Security;
using SmartVitals_Server.Models.json;
using SmartVitals_Server.Models;
using Telerik.Web.Mvc;
using SmartVitals.Services.Models;
using SmartVitals_Server.Context;

namespace SmartVitals_Server.Controllers
{
    public class JsonController : Controller
    {
        public JsonResult AuthenticateUser(string userName, string password, string deviceId)
        {
            try
            {
                var authenticated = Membership.ValidateUser(userName, password);
                if (authenticated)
                {
                    var sessionGuid = Guid.NewGuid();
                    var session = new Session()
                    {
                        DeviceId = deviceId,
                        SessionGuid = sessionGuid,
                        UserName = userName,
                        OpenedOn = DateTime.Now,
                        IsValid = true,
                        ClosedOn = DateTime.Now.AddYears(1)
                    };
                    DbHelpers.AddSession(session);
                    var response = new AuthenticationToken
                        {
                            IsAuthenticated = authenticated,
                            Message = "Success",
                            Role = Roles.GetRolesForUser(userName).Equals("Administrator")
                            ? SmartVitals.Services.Enums.Roles.Administrator.ToString()
                            : SmartVitals.Services.Enums.Roles.User.ToString(),
                            Profile = UserInfo.GetFromProfile(WebProfile.GetProfile(userName, authenticated)),
                            SessionId = authenticated ? sessionGuid : new Guid()
                        };
                    return Json(
                   new JsonResponse<AuthenticationToken> { Response = response, Success = true },
                       JsonRequestBehavior.AllowGet
                );
                }
                return Json(
                   new JsonResponse<AuthenticationToken> { Response = null, Success = false, Message = "Invalid username or password" },
                       JsonRequestBehavior.AllowGet
                );
            }
            catch (Exception)
            {
                return Json(
                     new JsonResponse<AuthenticationToken> { Response = null, Success = false, Message = "An error occured while processing your request." },
                         JsonRequestBehavior.AllowGet
                  );
            }
        }

        public JsonResult DeauthenticateUser(string SessionGuid)
        {
            try
            {

                DbHelpers.RemoveSession(SessionGuid);
                return Json(
                     new JsonResponse<Boolean> { Response = true, Success = true, Message = "This device has been deauthenticated." },
                         JsonRequestBehavior.AllowGet
                  );
            }
            catch (Exception)
            {
                return Json(
                     new JsonResponse<Boolean> { Response = false, Success = false, Message = "An error occured while processing your request." },
                         JsonRequestBehavior.AllowGet
                  );
            }
        }

        public JsonResult GetAuthenticationToken(string SessionGuid)
        {
            try
            {
                var selectedSession = DbHelpers.GetSessionByGuid(SessionGuid);
                if (selectedSession != null)
                {
                    var response = new AuthenticationToken
                    {
                        IsAuthenticated = selectedSession.IsValid,
                        Message = "Success",
                        Role = Roles.GetRolesForUser(selectedSession.UserName).Equals("Administrator")
                        ? SmartVitals.Services.Enums.Roles.Administrator.ToString()
                        : SmartVitals.Services.Enums.Roles.User.ToString(),
                        Profile = UserInfo.GetFromProfile(WebProfile.GetProfile(selectedSession.UserName, selectedSession.IsValid)),
                        SessionId = selectedSession.IsValid ? new Guid(SessionGuid) : new Guid()
                    };
                    return Json(
                        new JsonResponse<AuthenticationToken> { Response = response, Success = true },
                        JsonRequestBehavior.AllowGet
                    );
                }
                return Json(
                   new JsonResponse<AuthenticationToken> { Response = null, Success = true, Message = "Expired or invalid session." },
                       JsonRequestBehavior.AllowGet
                );
            }
            catch (Exception)
            {
                return Json(
                     new JsonResponse<AuthenticationToken> { Response = null, Success = false, Message = "An error occured while processing your request." },
                         JsonRequestBehavior.AllowGet
                  );
            }
        }

        public JsonResult UpdateProfile(string SessionGuid, UserInfo Profile)
        {
            try
            {
                var response = Profile;
                var validSession = DbHelpers.SessionIsValid(SessionGuid);
                if (!validSession)
                {
                    return Json(
                        new JsonResponse<bool>
                        {
                            Response = false,
                            Success = false,
                            Message = "Invalid Session. Try logging out and then loggin back in. No changes were saved."
                        },
                       JsonRequestBehavior.AllowGet
                    );
                }
                bool Success = false;
                if (Profile == null)
                {
                    return Json(
                        new JsonResponse<bool>
                        {
                            Response = Success,
                            Success = false,
                            Message = "Profile==null"
                        },
                       JsonRequestBehavior.AllowGet
                    );
                }
                UserInfo.Save(Profile);
                Success = true;
                return Json(
                    new JsonResponse<bool>
                    {
                        Response = Success,
                        Success = true,
                        Message = "User Updated!"
                    },
                   JsonRequestBehavior.AllowGet
                );
            }
            catch (Exception e)
            {
                return Json(
                     new JsonResponse<bool>
                     {
                         Response = false,
                         Success = false,
                         Message = e.Message
                     },
                    JsonRequestBehavior.AllowGet
                 );
            }
        }

        public JsonResult AddTest(string SessionGuid, Test Test)
        {
            try
            {
                var validSession = DbHelpers.SessionIsValid(SessionGuid);
                if (!validSession)
                {
                    return Json(
                        new JsonResponse<bool>
                        {
                            Response = false,
                            Success = false,
                            Message = "Invalid Session. Try logging out and then loggin back in. No changes were saved."
                        },
                       JsonRequestBehavior.AllowGet
                    );
                }
                bool Success = false;
                if (Test == null)
                {
                    return Json(
                        new JsonResponse<bool>
                        {
                            Response = Success,
                            Success = false,
                            Message = "Test==null"
                        },
                       JsonRequestBehavior.AllowGet
                    );
                }

                Success = DbHelpers.AddTest(Test);
                return Json(
                    new JsonResponse<bool>
                    {
                        Response = Success,
                        Success = Success,
                        Message = Success?"Test added!": "An error occurred while adding the test."
                    },
                   JsonRequestBehavior.AllowGet
                );
            }
            catch (Exception e)
            {
                return Json(
                     new JsonResponse<bool>
                     {
                         Response = false,
                         Success = false,
                         Message = e.Message
                     },
                    JsonRequestBehavior.AllowGet
                 );
            }
        }
    }
}
