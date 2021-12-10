function Authenticate() {
            $.blockUI({ css: {
                border: 'none',
                padding: '15px',
                backgroundColor: '#000',
                '-webkit-border-radius': '10px',
                '-moz-border-radius': '10px',
                opacity: .5,
                color: '#fff'
            }
            }); 
            var u = $('#userName').val().toString();
            var p = $('#password').val().toString();
            $.ajax({
                url: 'Account/Login?username=' + u + '&password=' + p,
                type: 'GET',
                success: function (result) {
                    if (result) {
                        if(getParameterByName('ReturnUrl')!=''){
                            $(location).attr('href', getParameterByName('ReturnUrl'));
                            $.unblockUI();
                        }
                        else{
                            $(location).attr('href', 'Home/Portal');
                            //$.unblockUI();
                        }
                    }
                    else {
                        $.unblockUI();
                        $.growlUI('Invalid username or password.');
                    }
                },
                error: function (error) {
                    $.unblockUI();
                    $.growlUI('An error occured submitting your request.', 'Please try again.');
                },
            });
}

function onError(error)
{
     $.growlUI(error);
}

function Message(msg)
{
    alert(msg);
 //   $.growlUI(msg);
}

function getParameterByName(name)
{
  name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
  var regexS = "[\\?&]" + name + "=([^&#]*)";
  var regex = new RegExp(regexS);
  var results = regex.exec(window.location.search);
  if(results == null)
    return "";
  else
    return decodeURIComponent(results[1].replace(/\+/g, " "));
}

function goHome(){
    location.href="http://www.smartvitals.com"
 }