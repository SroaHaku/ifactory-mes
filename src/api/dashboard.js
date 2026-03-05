import request from '@/utils/request'

/**
 * 获取生产汇总数据
 */
export function getProductionSummary() {
  return request({
    url: '/dashboard/summary',
    method: 'get'
  })
}

/**
 * 获取设备状态数据
 */
export function getEquipmentStatus() {
  return request({
    url: '/dashboard/equipment/status',
    method: 'get'
  })
}

/**
 * 获取质量统计数据
 */
export function getQualityStatistics() {
  return request({
    url: '/dashboard/quality/statistics',
    method: 'get'
  })
}
