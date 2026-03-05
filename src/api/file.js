import request from '@/utils/request'

/**
 * 上传文件
 * @param {FormData} data - 包含文件和类型的FormData对象
 */
export function uploadFile(data) {
  return request({
    url: '/file/upload',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 获取文件列表
 * @param {Object} params - 查询参数
 * @param {string} params.type - 文件类型
 * @param {number} params.page - 页码
 * @param {number} params.size - 每页数量
 */
export function getFileList(params) {
  return request({
    url: '/file/list',
    method: 'get',
    params
  })
}

/**
 * 下载文件
 * @param {number} id - 文件ID
 */
export function downloadFile(id) {
  return request({
    url: `/file/download/${id}`,
    method: 'get',
    responseType: 'blob' // 重要，用于下载文件
  })
}

/**
 * 删除文件
 * @param {number} id - 文件ID
 */
export function deleteFile(id) {
  return request({
    url: `/file/${id}`,
    method: 'delete'
  })
}
