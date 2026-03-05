import request from '@/utils/request'

/**
 * 获取报表列表
 * @param {Object} params - 查询参数
 * @param {string} params.type - 报表类型
 * @param {number} params.page - 页码
 * @param {number} params.size - 每页数量
 */
export function getReportList(params) {
  return request({
    url: '/report/list',
    method: 'get',
    params
  })
}

/**
 * 获取报表详情
 * @param {number} id - 报表ID
 */
export function getReportDetail(id) {
  return request({
    url: `/report/${id}`,
    method: 'get'
  })
}

/**
 * 生成报表
 * @param {Object} data - 报表生成参数
 */
export function generateReport(data) {
  return request({
    url: '/report/generate',
    method: 'post',
    data
  })
}

/**
 * 导出报表
 * @param {number} id - 报表ID
 * @param {string} format - 导出格式，默认excel
 */
export function exportReport(id, format = 'excel') {
  return request({
    url: `/report/export/${id}`,
    method: 'get',
    params: { format },
    responseType: 'blob' // 重要，用于下载文件
  })
}

/**
 * 删除报表
 * @param {number} id - 报表ID
 */
export function deleteReport(id) {
  return request({
    url: `/report/${id}`,
    method: 'delete'
  })
}
