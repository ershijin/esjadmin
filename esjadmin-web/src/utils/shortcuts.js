import Date from './datetime.js'

export const calendarBaseShortcuts = [{
  text: '今天',
  onClick(picker) {
    const start = new Date().toMidnight()
    const end = new Date()
    picker.$emit('pick', [start, end])
  }
}, {
  text: '昨天',
  onClick(picker) {
    const start = new Date().daysAgo(1, true)
    const end = new Date().toMidnight().secsAgo(1)
    picker.$emit('pick', [start, end])
  }
}, {
  text: '最近一周',
  onClick(picker) {
    const start = new Date().daysAgo(7)
    picker.$emit('pick', [start, new Date()])
  }
}, {
  text: '最近30天',
  onClick(picker) {
    const start = new Date().daysAgo(30)
    picker.$emit('pick', [start, new Date()])
  }
}, {
  text: '这个月',
  onClick(picker) {
    const start = new Date().monthBegin()
    picker.$emit('pick', [start, new Date()])
  }
}, {
  text: '本季度',
  onClick(picker) {
    const start = new Date().quarterBegin()
    picker.$emit('pick', [start, new Date()])
  }
}]

export const calendarMoveShortcuts = [{
  text: '‹ 往前一天 ',
  onClick(picker) {
    if (picker.value == null || picker.value.length === 0) {
      picker.value = [new Date(), new Date()]
    }
    const start = picker.value[0].daysAgo(1)
    const end = picker.value[1].daysAgo(1)
    picker.$emit('pick', [start, end])
  }
}, {
  text: ' 往后一天 ›',
  onClick(picker) {
    let start = new Date()
    let end = new Date()
    if (picker.value != null && picker.value.length > 0) {
      if (end - picker.value[1] > 8.64E7) {
        start = picker.value[0].daysAgo(-1)
        end = picker.value[1].daysAgo(-1)
      } else {
        start = picker.value[0]
      }
    }
    picker.$emit('pick', [start, end])
  }
}, {
  text: '« 往前一周 ',
  onClick(picker) {
    if (picker.value == null || picker.value.length === 0) {
      picker.value = [new Date().daysAgo(7), new Date()]
    }
    const start = picker.value[0].daysAgo(7)
    const end = picker.value[1].daysAgo(7)
    picker.$emit('pick', [start, end])
  }
}, {
  text: ' 往后一周 »',
  onClick(picker) {
    let start = new Date().daysAgo(7)
    let end = new Date()
    if (picker.value != null && picker.value.length > 0) {
      if (end - picker.value[1] > 8.64E7) {
        start = picker.value[0].daysAgo(-7)
        end = picker.value[1].daysAgo(-7)
      } else {
        start = picker.value[0]
      }
    }
    picker.$emit('pick', [start, end])
  }
}]

export const calendarShortcuts = [
  ...calendarBaseShortcuts,
  ...calendarMoveShortcuts
]
