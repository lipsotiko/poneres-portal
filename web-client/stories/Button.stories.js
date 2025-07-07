import { Meta, StoryObj } from '@storybook/vue3'

import { Button } from './components/ui/button'

// More on how to set up stories at: https://storybook.js.org/docs/vue/writing-stories/introduction

const meta = {
  title: 'Example/Button',
  component: Button,
} satisfies Meta<typeof Button>

export default meta
type Story = StoryObj<typeof meta>

export const Default: Story = {
  args: {},
}