


// calendar
const Calendar = tui.Calendar;
const container = document.getElementById('calendar');
let employeeNum = document.getElementById("sessionEmployeeNum").value;
const deptListUrl = '/api/mainpage/dept'
let deptList = []
fetch(deptListUrl, {
    method: 'GET',
    headers: {
        "Content-Type": "application/json",
    }
}).then(data => {
    return data.json()
}).then((data) => {
    deptList = data
    const color = [
        '#A7C7E7', '#98FB98', '#FADADD', '#FFDAB9',
        '#C6A2D9', '#F5E6CC',
        '#A7C7E7', '#98FB98', '#FADADD', '#FFFACD', '#E6E6FA', '#D3D3D3',
        '#A7C7E7', '#98FB98', '#FADADD', '#FFFACD', '#E6E6FA', '#D3D3D3',
    ]

    let scheduleType = data.map((dept, idx) => {
        return {
            id: dept.departmentNum,
            name: dept.departmentName,
            backgroundColor: color[idx]
        }
    })
    scheduleType.push({
        id: 0,
        name: '개인',
        backgroundColor: color[scheduleType.length]
    })

    let calendar = new Calendar(container,  {
        defaultView: 'month',
        isReadOnly: false,
        timezone: {
            zones: [
                {
                    timezoneName: 'Asia/Seoul',
                    displayLabel: 'Seoul',
                },
            ],
        },
        calendars: scheduleType,
        theme: {
            common: {
                gridSelection: {
                    backgroundColor: 'rgba(21, 30, 92, 0.05)',
                    border: '1px solid A7C7E7',
                },
            },
        },
        template: {
            time(event) {
                const { start, end, title } = event;

                return `<span style="color: black;">${formatTime(start)}~${formatTime(end)} ${title}</span>`;
            },
            allday(event) {
                return `<span style="color: black;">${event.title}</span>`;
            },
            monthMoreClose() {
                return '';
            },
            monthGridHeaderExceed(hiddenEvents) {
                return `<span>${hiddenEvents} +</span>`;
            },
            monthDayName(model) {
                const dayKor = {
                    0: '일 ❤',
                    1: '월',
                    2: '화',
                    3: '수',
                    4: '목',
                    5: '금',
                    6: '토 💙',
                }
                return dayKor[model.day];
            },
            titlePlaceholder() {
                return '내용';
            },
            locationPlaceholder() {
                return '장소';
            },
            startDatePlaceholder() {
                return '시작 날짜';
            },
            endDatePlaceholder() {
                return '종료 날짜';
            },
            popupSave() {
                return '<div style="background-color: #4B49AC;width: 100px;height: 40px;border-radius: 80px;position: absolute;bottom: 19px;right: 16px;display: flex;justify-content: center;align-items: center;font-size: 14px;">추가</div>';
            },
            popupUpdate() {
                return '<div style="background-color: #4B49AC;width: 100px;height: 40px;border-radius: 80px;position: absolute;bottom: 19px;right: 16px;display: flex;justify-content: center;align-items: center;font-size: 14px;">변경</div>';
            },
            popupEdit() {
                return '변경';
            },
            popupDelete() {
                return '<span data-bs-toggle="modal" data-bs-target="#commonModal">삭제</span>';
            },
            monthSchedule: function(schedule) {
                // 이벤트 시작 및 종료 날짜 가져오기
                const startDate = new Date(schedule.start);
                const endDate = new Date(schedule.end);
                console.log('schedule', schedule)
                // 하루짜리 일정이면 시간 + 제목, 그렇지 않으면 제목만 표시
                if (endDate.getDate() > startDate.getDate() ||
                    endDate.getMonth() > startDate.getMonth() ||
                    endDate.getFullYear() > startDate.getFullYear()) {
                    console.log(schedule.title)
                    return schedule.title; // 시간 없이 제목만 반환
                } else {
                    return `${startDate.getHours()}:${String(startDate.getMinutes()).padStart(2, '0')} ${schedule.title}`;
                }
            }
        },
        useFormPopup: true,
        useDetailPopup: true,
    });
    const url = 'api/mainpage/schedule'
    fetch(url, {
        method: 'get',
        headers: {
            "Content-Type": "application/json",
        },
    }).then((result) => {
        return result.json()
    }).then((data) => {
        let schedules = []

        data.forEach((schedule) => {
            // private schedule(lock icon) && not my schedule => false
            if(!(schedule.isPrivate === 'Y' && schedule.employeeNum.toString() !== employeeNum)) {
                schedules.push({
                    id: schedule.scheduleId,
                    calendarId: schedule.scheduleType === 'DEPT' ? schedule.deptNo : 0,
                    title: schedule.scheduleContent,
                    start: schedule.scheduleStart,
                    end: schedule.scheduleEnd,
                    isAllday: schedule.isAllday === 'Y',
                    isPrivate: schedule.isPrivate === 'Y',
                    location: schedule.location,
                    attendees: getDeptName(deptList, schedule.deptNo)
                })
            }

        })

        calendar.createEvents(schedules)
    })

    calendar.on('beforeCreateEvent', (eventObj) => {
        addSchedule(calendar, eventObj)
    });

    calendar.on('beforeDeleteEvent', (eventObj) => {
        setModalContent('일정 삭제', 'delete-schedule', '닫기', '삭제', () => deleteSchedule(calendar, eventObj))
    });

    calendar.on('beforeUpdateEvent', ({event, changes}) => {
        updateSchedule(calendar, event, changes)
    });
})

const updateSchedule = (calendar, event, changes) => {
    let updatedEvent = {
        ...event,
        ...changes
    }
    let updatedSchedule = {
        deptNo: updatedEvent.calendarId,
        scheduleEnd: updatedEvent.end.d.d,
        scheduleStart: updatedEvent.start.d.d,
        scheduleType: updatedEvent.calendarId ? 'DEPT' : 'PERSONAL',
        scheduleContent: updatedEvent.title,
        isAllday: updatedEvent.isAllday ? 'Y' : 'N',
        isPrivate: updatedEvent.isPrivate ? 'Y' : 'N',
        location: updatedEvent.location
    }
    const url = `api/mainpage/schedule/${updatedEvent.id}`
    fetch(url, {
        method: 'put',
        headers: {
            'header': header_csrf,
            "Content-Type": "application/json",
            'X-CSRF-Token': token_csrf
        },
        body: JSON.stringify(updatedSchedule)
    }).then((result) => {
        return result.json()
    }).then((data) => {
        if(data.message === 'success') {
            calendar.updateEvent(event.id, event.calendarId, changes);
            showAlert('일정이 수정되었습니다.', 'success')
        } else {
            showAlert('일정 수정 실패', 'danger')
        }
    })
}


const deleteSchedule = (calendar, eventObj) => {
    const url = `api/mainpage/schedule/${eventObj.id}`
    fetch(url, {
        method: 'delete',
        headers: {
            'header': header_csrf,
            "Content-Type": "application/json",
            'X-CSRF-Token': token_csrf
        },
    }).then((result) => {
        return result.json()
    }).then((data) => {
        if(data.message === 'success') {
            calendar.deleteEvent(eventObj.id, eventObj.calendarId);
            showAlert('일정이 삭제되었습니다.', 'success')
        } else {
            showAlert('일정 삭제 실패', 'danger')
        }
    })

}

const addSchedule = (calendar, eventObj) => {
    let newSchedule = {
        deptNo: eventObj.calendarId,
        scheduleEnd: eventObj.end.d.d,
        scheduleStart: eventObj.start.d.d,
        scheduleType: eventObj.calendarId ? 'DEPT' : 'PERSONAL',
        scheduleContent: eventObj.title,
        isAllday: eventObj.isAllday ? 'Y' : 'N',
        isPrivate: eventObj.isPrivate ? 'Y' : 'N',
        location: eventObj.location
    }
    const url = 'api/mainpage/schedule'
    fetch(url, {
        method: 'POST',
        headers: {
            'header': header_csrf,
            "Content-Type": "application/json",
            'X-CSRF-Token': token_csrf
        },
        body: JSON.stringify(newSchedule)
    }).then((result) => {
        return result.json()
    }).then((data) => {
        if(data.message === 'success') {
            calendar.createEvents([
                {
                    ...eventObj,
                    id: new Date().getTime(),
                },
            ]);

            showAlert('일정이 등록되었습니다.', 'success')
        } else {
            showAlert('일정 등록 실패', 'danger')
        }
    })}

const getDeptName = (deptList, scheduleDeptNo) => {
    if(!scheduleDeptNo) {
        return ['나']
    }

    let dept = deptList.find((dept) => dept.departmentNum === scheduleDeptNo)
    return [dept.departmentName]
}

function formatTime(time) {
    const hours = `${time.getHours()}`.padStart(2, '0');
    const minutes = `${time.getMinutes()}`.padStart(2, '0');

    return `${hours}:${minutes}`;
}
