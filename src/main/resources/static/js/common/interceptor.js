
(function () {

    // const xhttp = new XMLHttpRequest();
    //
    // xhttp.onload = function () {
    //     if (this.status === 401) {
    //         showAlert("세션이 만료되었습니다. 로그인 페이지로 이동합니다.", 'danger');
    //         setTimeout(() => {
    //             window.location.href = "/login";
    //         }, 3000);
    //     } else {
    //         try {
    //             if (typeof this.originalOnLoad === 'function') {
    //                 this.originalOnLoad.apply(this);
    //             }
    //         } catch (e) {
    //             console.log('originalOnLoad.apply(this); error', e);
    //         }
    //     }
    // };
    //
    // try {
    //     xhttp.send(body);
    // } catch (error) {
    //     console.log("[XHR Intercept] send() error:", error);
    // }










    // const xhttp = new XMLHttpRequest();
    //
    // xhttp.onload = function() {
    //     this.addEventListener("readystatechange", function () {
    //
    //         if (this.readyState === 4 && this.status !== 401) {
    //             try {
    //                 this.onreadystatechange.apply(this);
    //             }catch (e) {
    //                 console.log('originalOnReadyStateChange.apply(this); error')
    //             }
    //
    //         } else if(this.status === 401){
    //             showAlert("세션이 만료되었습니다. 로그인 페이지로 이동합니다.", 'danger');
    //             setTimeout(() => {
    //                 window.location.href = "/login";
    //             }, 3000);
    //         }
    //     });
    //     try {
    //         send(body);
    //     } catch (error) {
    //         console.log("[XHR Intercept] super.send() error:", error);
    //     }
    // }








        const originalXHR = window.XMLHttpRequest;

    class CustomXHR extends originalXHR {
        constructor() {
            super();
            this.addEventListener("readystatechange", function () {
                // console.log('this.readyState', this.readyState)
                // console.log('this.status', this.status)

                if (this.readyState === 4 && this.status === 401) {

                    showAlert("세션이 만료되었습니다. 로그인 페이지로 이동합니다.", 'danger');
                    setTimeout(() => {
                        window.location.href = "/login";
                    }, 1000);
                    return;
                } else if(this.readyState === 4 && this.status !== 401) {
                    // console.log("[XHR Intercept] Response Text:", this.responseText);
                    try {
                        // console.log("originalOnReadyStateChange:", originalOnReadyStateChange);
                        const originalOnReadyStateChange = this.onreadystatechange;
                        // 기존 핸들러가 존재하면 실행
                        if (typeof originalOnReadyStateChange === "function") {
                            try {
                                originalOnReadyStateChange.apply(this);
                            } catch (e) {
                                console.log('originalOnReadyStateChange.apply(this); error')
                            }
                        }
                    } catch (error) {
                        console.log("[XHR Intercept] Error in onreadystatechange:", error);
                    }
                }
            });
        }

        send(body) {
            // console.log('originalXHR')
            try {
                if (body === null) {
                    super.send();
                } else {
                    super.send(body);
                }
            } catch (error) {
                console.log("[XHR Intercept] super.send() error:", error);
            }
        }
    }

    window.XMLHttpRequest = CustomXHR;


    const originalFetch = window.fetch;
    window.fetch = async function (url, options) {
        const response = await originalFetch(url, {
            ...options,
        });

        if (response.status === 401) {
            showAlert("세션이 만료되었거나 로그인이 필요합니다.", 'danger');
            setTimeout(() => {
                window.location.href = "/login";
            }, 1000)
        } else {
            // console.log('fetch')
        }

        return response;
    };
})();