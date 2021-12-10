using System.Web.Mvc;

namespace SmartVitals_Server.Areas.Extensions
{
    public class ExtensionsAreaRegistration : AreaRegistration
    {
        public override string AreaName
        {
            get
            {
                return "Extensions";
            }
        }

        public override void RegisterArea(AreaRegistrationContext context)
        {
            context.MapRoute(
                "Extensions_default",
                "Extensions/{controller}/{action}/{id}",
                new { action = "Index", id = UrlParameter.Optional }
            );
        }
    }
}
